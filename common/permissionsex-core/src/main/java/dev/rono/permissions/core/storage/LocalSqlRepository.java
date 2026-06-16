package dev.rono.permissions.core.storage;

import dev.rono.permissions.core.storage.model.Group;
import dev.rono.permissions.core.storage.model.GroupInheritance;
import dev.rono.permissions.core.storage.model.GroupOptions;
import dev.rono.permissions.core.storage.model.GroupPermission;
import dev.rono.permissions.core.storage.model.Ladder;
import dev.rono.permissions.core.storage.model.LadderGroup;
import dev.rono.permissions.core.storage.model.User;
import dev.rono.permissions.core.storage.model.UserGroup;
import dev.rono.permissions.core.storage.model.UserOptions;
import dev.rono.permissions.core.storage.model.UserPermission;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class LocalSqlRepository implements AutoCloseable {

    public static final int SCHEMA_VERSION = 1;

    private final BasicDataSource dataSource;

    public LocalSqlRepository(String jdbcUrl, String user, String password) {
        this.dataSource = new BasicDataSource();
        this.dataSource.setDriverClassName("org.h2.Driver");
        this.dataSource.setUrl(jdbcUrl);
        this.dataSource.setUsername(user == null ? "" : user);
        this.dataSource.setPassword(password == null ? "" : password);
        this.dataSource.setMaxTotal(4);
    }

    public static LocalSqlRepository fileDatabase(String path) {
        String normalized = path.replace('\\', '/');
        return new LocalSqlRepository("jdbc:h2:file:" + normalized + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE", "", "");
    }

    public static LocalSqlRepository inMemory(String name) {
        return new LocalSqlRepository("jdbc:h2:mem:" + name + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE", "", "");
    }

    public DataSource dataSource() {
        return dataSource;
    }

    public void deploySchema() throws SQLException, IOException {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            for (String sql : loadStatements("/sql/local/deploy.sql")) {
                stmt.execute(sql);
            }
        }
    }

    public int getSchemaVersion() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            if (!tableExists(conn, "schema_meta")) {
                return -1;
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT meta_value FROM schema_meta WHERE meta_key = 'schema_version'")) {
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return -1;
                }
                return Integer.parseInt(rs.getString("meta_value"));
            }
        }
    }

    public void setSchemaVersion(int version) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "MERGE INTO schema_meta (meta_key, meta_value) KEY(meta_key) VALUES ('schema_version', ?)")) {
            ps.setString(1, Integer.toString(version));
            ps.executeUpdate();
        }
    }

    public boolean isInitialized() throws SQLException {
        return tableExists(dataSource.getConnection(), "users") && getSchemaVersion() >= 0;
    }

    public void upsertUser(UUID id, String name, Instant firstJoin, Instant lastSeen) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "MERGE INTO users (id, name, first_join, last_seen) KEY(id) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, id.toString());
            ps.setString(2, name);
            ps.setTimestamp(3, toTimestamp(firstJoin));
            ps.setTimestamp(4, toTimestamp(lastSeen != null ? lastSeen : Instant.now()));
            ps.executeUpdate();
        }
    }

    public Optional<User> findUserByName(String name) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM users WHERE LOWER(name) = LOWER(?)")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(loadUser(UUID.fromString(rs.getString("id"))));
        }
    }

    public Optional<User> findUserById(UUID id) throws SQLException {
        if (!userExists(id)) {
            return Optional.empty();
        }
        return Optional.of(loadUser(id));
    }

    public boolean userExists(UUID id) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM users WHERE id = ?")) {
            ps.setString(1, id.toString());
            return ps.executeQuery().next();
        }
    }

    public boolean userExistsByName(String name) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM users WHERE LOWER(name) = LOWER(?)")) {
            ps.setString(1, name);
            return ps.executeQuery().next();
        }
    }

    public User loadUser(UUID id) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT name, first_join, last_seen FROM users WHERE id = ?")) {
            ps.setString(1, id.toString());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Unknown user " + id);
            }
            String name = rs.getString("name");
            Instant firstJoin = toInstant(rs.getTimestamp("first_join"));
            Instant lastSeen = toInstant(rs.getTimestamp("last_seen"));
            return new User(id, name, firstJoin, lastSeen, loadUserGroups(conn, id), loadUserPermissions(conn, id),
                    loadUserOptions(conn, id));
        }
    }

    public int upsertGroup(String name, int weight, boolean isDefault) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            Optional<Integer> existing = findGroupId(conn, name);
            if (existing.isPresent()) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE \"groups\" SET weight = ?, is_default = ? WHERE id = ?")) {
                    ps.setInt(1, weight);
                    ps.setBoolean(2, isDefault);
                    ps.setInt(3, existing.get());
                    ps.executeUpdate();
                }
                return existing.get();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO \"groups\" (name, weight, is_default) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setInt(2, weight);
                ps.setBoolean(3, isDefault);
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (!keys.next()) {
                    throw new SQLException("Failed to create group " + name);
                }
                return keys.getInt(1);
            }
        }
    }

    public Optional<Integer> findGroupId(String name) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return findGroupId(conn, name);
        }
    }

    public Optional<Group> findGroupByName(String name) throws SQLException {
        Optional<Integer> id = findGroupId(name);
        return id.map(this::loadGroupUnchecked);
    }

    public Group loadGroup(int groupId) throws SQLException {
        Group group = loadGroupUnchecked(groupId);
        if (group == null) {
            throw new SQLException("Unknown group " + groupId);
        }
        return group;
    }

    public Map<Integer, Group> loadAllGroups() throws SQLException {
        Map<Integer, Group> out = new HashMap<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM \"groups\"");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                out.put(id, loadGroup(conn, id));
            }
        }
        return out;
    }

    public List<String> listUserNames() throws SQLException {
        List<String> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT name FROM users ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(rs.getString("name"));
            }
        }
        return out;
    }

    public List<String> listGroupNames() throws SQLException {
        List<String> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT name FROM \"groups\" ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(rs.getString("name"));
            }
        }
        return out;
    }

    public List<Ladder> loadLadders() throws SQLException {
        List<Ladder> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, name FROM ladders ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                out.add(new Ladder(id, rs.getString("name"), loadLadderGroups(conn, id)));
            }
        }
        return out;
    }

    public void setUserGroup(UUID userId, int groupId, Instant expiresAt) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "MERGE INTO user_groups (user_id, group_id, expires_at) KEY(user_id, group_id) VALUES (?, ?, ?)")) {
            ps.setString(1, userId.toString());
            ps.setInt(2, groupId);
            ps.setTimestamp(3, toTimestamp(expiresAt));
            ps.executeUpdate();
        }
    }

    public void clearUserGroups(UUID userId, String contextKey) throws SQLException {
        if (contextKey != null) {
            return;
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM user_groups WHERE user_id = ?")) {
            ps.setString(1, userId.toString());
            ps.executeUpdate();
        }
    }

    public void setGroupParent(int groupId, int parentId) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "MERGE INTO group_inheritance (group_id, parent_id) KEY(group_id, parent_id) VALUES (?, ?)")) {
            ps.setInt(1, groupId);
            ps.setInt(2, parentId);
            ps.executeUpdate();
        }
    }

    public void clearGroupParents(int groupId) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM group_inheritance WHERE group_id = ?")) {
            ps.setInt(1, groupId);
            ps.executeUpdate();
        }
    }

    public void replaceUserPermissions(UUID userId, String contextKey, List<String> permissions) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            deleteUserPermissions(conn, userId, contextKey);
            for (String permission : permissions) {
                boolean allow = !permission.startsWith("-");
                String node = allow ? permission : permission.substring(1);
                addUserPermission(conn, userId, node, allow, contextKey, null);
            }
        }
    }

    public void replaceGroupPermissions(int groupId, String contextKey, List<String> permissions) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            deleteGroupPermissions(conn, groupId, contextKey);
            for (String permission : permissions) {
                boolean allow = !permission.startsWith("-");
                String node = allow ? permission : permission.substring(1);
                addGroupPermission(conn, groupId, node, allow, contextKey, null);
            }
        }
    }

    public void setUserOption(UUID userId, String key, String value) throws SQLException {
        if (!"prefix".equals(key) && !"suffix".equals(key)) {
            return;
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "MERGE INTO user_options (user_id, prefix, suffix) KEY(user_id) VALUES (?, ?, ?)")) {
            UserOptions current = loadUserOptions(conn, userId);
            String prefix = current != null ? current.getPrefix() : null;
            String suffix = current != null ? current.getSuffix() : null;
            if ("prefix".equals(key)) {
                prefix = value;
            } else {
                suffix = value;
            }
            ps.setString(1, userId.toString());
            ps.setString(2, prefix);
            ps.setString(3, suffix);
            ps.executeUpdate();
        }
    }

    public void setGroupOption(int groupId, String key, String value) throws SQLException {
        if (!"prefix".equals(key) && !"suffix".equals(key)) {
            return;
        }
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "MERGE INTO group_options (group_id, prefix, suffix) KEY(group_id) VALUES (?, ?, ?)")) {
            GroupOptions current = loadGroupOptions(conn, groupId);
            String prefix = current != null ? current.getPrefix() : null;
            String suffix = current != null ? current.getSuffix() : null;
            if ("prefix".equals(key)) {
                prefix = value;
            } else {
                suffix = value;
            }
            ps.setInt(1, groupId);
            ps.setString(2, prefix);
            ps.setString(3, suffix);
            ps.executeUpdate();
        }
    }

    public int upsertLadder(String name) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement find = conn.prepareStatement("SELECT id FROM ladders WHERE name = ?")) {
                find.setString(1, name);
                ResultSet rs = find.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            try (PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO ladders (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                insert.setString(1, name);
                insert.executeUpdate();
                ResultSet keys = insert.getGeneratedKeys();
                if (!keys.next()) {
                    throw new SQLException("Failed to create ladder " + name);
                }
                return keys.getInt(1);
            }
        }
    }

    public void setLadderGroup(int ladderId, int groupId, int position) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "MERGE INTO ladder_groups (ladder_id, group_id, position) KEY(ladder_id, group_id) VALUES (?, ?, ?)")) {
            ps.setInt(1, ladderId);
            ps.setInt(2, groupId);
            ps.setInt(3, position);
            ps.executeUpdate();
        }
    }

    public Set<String> listPermissionWorlds(UUID userId) throws SQLException {
        Set<String> worlds = new LinkedHashSet<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT DISTINCT context_key FROM user_permissions WHERE user_id = ?")) {
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                worlds.add(ContextKeyCodec.decodeWorld(rs.getString("context_key")));
            }
        }
        worlds.remove(null);
        return worlds;
    }

    public Set<String> listGroupPermissionWorlds(int groupId) throws SQLException {
        Set<String> worlds = new LinkedHashSet<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT DISTINCT context_key FROM group_permissions WHERE group_id = ?")) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                worlds.add(ContextKeyCodec.decodeWorld(rs.getString("context_key")));
            }
        }
        worlds.remove(null);
        return worlds;
    }

    public List<String> getUserPermissions(UUID userId, String contextKey) throws SQLException {
        List<String> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT permission, allow FROM user_permissions WHERE user_id = ? AND "
                             + "(context_key IS NULL AND ? IS NULL OR context_key = ?)")) {
            ps.setString(1, userId.toString());
            ps.setString(2, contextKey);
            ps.setString(3, contextKey);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String node = rs.getString("permission");
                if (!rs.getBoolean("allow")) {
                    node = "-" + node;
                }
                out.add(node);
            }
        }
        return out;
    }

    public List<String> getGroupPermissions(int groupId, String contextKey) throws SQLException {
        List<String> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT permission, allow FROM group_permissions WHERE group_id = ? AND "
                             + "(context_key IS NULL AND ? IS NULL OR context_key = ?)")) {
            ps.setInt(1, groupId);
            ps.setString(2, contextKey);
            ps.setString(3, contextKey);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String node = rs.getString("permission");
                if (!rs.getBoolean("allow")) {
                    node = "-" + node;
                }
                out.add(node);
            }
        }
        return out;
    }

    public List<String> getUserParents(UUID userId) throws SQLException {
        List<String> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT g.name FROM user_groups ug JOIN \"groups\" g ON g.id = ug.group_id "
                             + "WHERE ug.user_id = ? AND (ug.expires_at IS NULL OR ug.expires_at > CURRENT_TIMESTAMP)")) {
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(rs.getString("name"));
            }
        }
        return out;
    }

    public List<String> getGroupParents(int groupId) throws SQLException {
        List<String> out = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT g.name FROM group_inheritance gi JOIN \"groups\" g ON g.id = gi.parent_id WHERE gi.group_id = ?")) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(rs.getString("name"));
            }
        }
        return out;
    }

    public void deleteUser(UUID userId) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            ps.setString(1, userId.toString());
            ps.executeUpdate();
        }
    }

    public void deleteGroup(int groupId) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM \"groups\" WHERE id = ?")) {
            ps.setInt(1, groupId);
            ps.executeUpdate();
        }
    }

    public boolean isEmpty() throws SQLException {
        return listUserNames().isEmpty() && listGroupNames().isEmpty();
    }

    @Override
    public void close() {
        try {
            dataSource.close();
        } catch (SQLException ignored) {
        }
    }

    private Group loadGroupUnchecked(int groupId) {
        try (Connection conn = dataSource.getConnection()) {
            return loadGroup(conn, groupId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Group loadGroup(Connection conn, int groupId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT name, weight, is_default FROM \"groups\" WHERE id = ?")) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return new Group(groupId, rs.getString("name"), rs.getInt("weight"), rs.getBoolean("is_default"),
                    loadGroupPermissions(conn, groupId), loadGroupInheritance(conn, groupId),
                    loadGroupOptions(conn, groupId));
        }
    }

    private List<UserGroup> loadUserGroups(Connection conn, UUID userId) throws SQLException {
        List<UserGroup> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT group_id, expires_at FROM user_groups WHERE user_id = ?")) {
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new UserGroup(userId, rs.getInt("group_id"), toInstant(rs.getTimestamp("expires_at"))));
            }
        }
        return out;
    }

    private List<UserPermission> loadUserPermissions(Connection conn, UUID userId) throws SQLException {
        List<UserPermission> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT permission, allow, context_key, expires_at FROM user_permissions WHERE user_id = ?")) {
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new UserPermission(userId, rs.getString("permission"), rs.getBoolean("allow"),
                        rs.getString("context_key"), toInstant(rs.getTimestamp("expires_at"))));
            }
        }
        return out;
    }

    private List<GroupPermission> loadGroupPermissions(Connection conn, int groupId) throws SQLException {
        List<GroupPermission> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT permission, allow, context_key, expires_at FROM group_permissions WHERE group_id = ?")) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new GroupPermission(groupId, rs.getString("permission"), rs.getBoolean("allow"),
                        rs.getString("context_key"), toInstant(rs.getTimestamp("expires_at"))));
            }
        }
        return out;
    }

    private List<GroupInheritance> loadGroupInheritance(Connection conn, int groupId) throws SQLException {
        List<GroupInheritance> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT parent_id FROM group_inheritance WHERE group_id = ?")) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new GroupInheritance(groupId, rs.getInt("parent_id")));
            }
        }
        return out;
    }

    private List<LadderGroup> loadLadderGroups(Connection conn, int ladderId) throws SQLException {
        List<LadderGroup> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT group_id, position FROM ladder_groups WHERE ladder_id = ? ORDER BY position")) {
            ps.setInt(1, ladderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                out.add(new LadderGroup(ladderId, rs.getInt("group_id"), rs.getInt("position")));
            }
        }
        return out;
    }

    private UserOptions loadUserOptions(Connection conn, UUID userId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT prefix, suffix FROM user_options WHERE user_id = ?")) {
            ps.setString(1, userId.toString());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return new UserOptions(null, null);
            }
            return new UserOptions(rs.getString("prefix"), rs.getString("suffix"));
        }
    }

    private GroupOptions loadGroupOptions(Connection conn, int groupId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT prefix, suffix FROM group_options WHERE group_id = ?")) {
            ps.setInt(1, groupId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return new GroupOptions(null, null);
            }
            return new GroupOptions(rs.getString("prefix"), rs.getString("suffix"));
        }
    }

    private void deleteUserPermissions(Connection conn, UUID userId, String contextKey) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM user_permissions WHERE user_id = ? AND "
                        + "(context_key IS NULL AND ? IS NULL OR context_key = ?)")) {
            ps.setString(1, userId.toString());
            ps.setString(2, contextKey);
            ps.setString(3, contextKey);
            ps.executeUpdate();
        }
    }

    private void deleteGroupPermissions(Connection conn, int groupId, String contextKey) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "DELETE FROM group_permissions WHERE group_id = ? AND "
                        + "(context_key IS NULL AND ? IS NULL OR context_key = ?)")) {
            ps.setInt(1, groupId);
            ps.setString(2, contextKey);
            ps.setString(3, contextKey);
            ps.executeUpdate();
        }
    }

    private void addUserPermission(Connection conn,
                                   UUID userId,
                                   String permission,
                                   boolean allow,
                                   String contextKey,
                                   Instant expiresAt) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO user_permissions (user_id, permission, allow, context_key, expires_at) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, userId.toString());
            ps.setString(2, permission);
            ps.setBoolean(3, allow);
            ps.setString(4, contextKey);
            ps.setTimestamp(5, toTimestamp(expiresAt));
            ps.executeUpdate();
        }
    }

    private void addGroupPermission(Connection conn,
                                    int groupId,
                                    String permission,
                                    boolean allow,
                                    String contextKey,
                                    Instant expiresAt) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO group_permissions (group_id, permission, allow, context_key, expires_at) VALUES (?, ?, ?, ?, ?)")) {
            ps.setInt(1, groupId);
            ps.setString(2, permission);
            ps.setBoolean(3, allow);
            ps.setString(4, contextKey);
            ps.setTimestamp(5, toTimestamp(expiresAt));
            ps.executeUpdate();
        }
    }

    private Optional<Integer> findGroupId(Connection conn, String name) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM \"groups\" WHERE LOWER(name) = LOWER(?)")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(rs.getInt("id"));
        }
    }

    private static boolean tableExists(Connection conn, String table) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, table.toUpperCase(Locale.ROOT), null)) {
            if (rs.next()) {
                return true;
            }
        }
        try (ResultSet rs = conn.getMetaData().getTables(null, null, table.toLowerCase(Locale.ROOT), null)) {
            return rs.next();
        }
    }

    private static List<String> loadStatements(String resource) throws IOException {
        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        try (InputStream in = LocalSqlRepository.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IOException("Missing resource " + resource);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                        continue;
                    }
                    current.append(line).append('\n');
                    if (trimmed.endsWith(";")) {
                        statements.add(current.toString());
                        current.setLength(0);
                    }
                }
            }
        }
        if (!current.isEmpty()) {
            statements.add(current.toString());
        }
        return statements;
    }

    private static Timestamp toTimestamp(Instant instant) {
        return instant == null ? null : Timestamp.from(instant);
    }

    private static Instant toInstant(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toInstant();
    }
}
