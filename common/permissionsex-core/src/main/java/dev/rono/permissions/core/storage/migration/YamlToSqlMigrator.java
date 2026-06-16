package dev.rono.permissions.core.storage.migration;

import dev.rono.permissions.core.backends.BackendDataTransfer;
import dev.rono.permissions.core.backends.file.YamlMaps;
import dev.rono.permissions.core.storage.ContextKeyCodec;
import dev.rono.permissions.core.storage.LocalSqlRepository;
import org.yaml.snakeyaml.Yaml;
import ru.tehkode.permissions.PermissionsGroupData;
import ru.tehkode.permissions.PermissionsUserData;
import ru.tehkode.permissions.backends.PermissionBackend;
import ru.tehkode.permissions.exceptions.PermissionBackendException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * One-way migration from legacy {@code permissions.yml} into the local H2 schema.
 */
public final class YamlToSqlMigrator {

    private final Logger logger;

    public YamlToSqlMigrator(Logger logger) {
        this.logger = logger;
    }

    public MigrationResult migrate(File yamlFile, LocalSqlRepository repository) throws Exception {
        if (!yamlFile.isFile()) {
            return MigrationResult.skipped("No YAML file at " + yamlFile.getAbsolutePath());
        }
        if (repository.isInitialized() && repository.getSchemaVersion() >= 0 && !repository.isEmpty()) {
            return MigrationResult.skipped("Local SQL database already contains data");
        }

        repository.deploySchema();
        repository.setSchemaVersion(LocalSqlRepository.SCHEMA_VERSION);

        Map<String, Object> root = loadYaml(yamlFile);
        Map<String, Object> groups = YamlMaps.getSection(root, YamlMaps.GROUPS);
        if (groups != null) {
            for (Map.Entry<String, Object> entry : groups.entrySet()) {
                if (entry.getValue() instanceof Map<?, ?> section) {
                    migrateGroup(repository, entry.getKey(), cast(section));
                }
            }
        }

        Map<String, Object> users = YamlMaps.getSection(root, YamlMaps.USERS);
        if (users != null) {
            for (Map.Entry<String, Object> entry : users.entrySet()) {
                if (entry.getValue() instanceof Map<?, ?> section) {
                    migrateUser(repository, entry.getKey(), cast(section));
                }
            }
        }

        File backup = new File(yamlFile.getParentFile(), yamlFile.getName() + ".migrated");
        Files.move(yamlFile.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
        logger.info("Migrated " + yamlFile.getName() + " to local SQL; backup at " + backup.getName());
        return MigrationResult.migrated(backup);
    }

    public static void migrateViaTransfer(PermissionBackend source, LocalSqlBackendAdapter target)
            throws PermissionBackendException {
        for (String groupName : source.getGroupNames()) {
            BackendDataTransfer.transferGroup(source.getGroupData(groupName), target.getGroupData(groupName));
        }
        for (String userName : source.getUserIdentifiers()) {
            BackendDataTransfer.transferUser(source.getUserData(userName), target.getUserData(userName));
        }
    }

    private void migrateGroup(LocalSqlRepository repository, String name, Map<String, Object> node) throws Exception {
        Map<String, String> options = YamlMaps.collectLeafOptions(YamlMaps.optionsMap(node, null));
        int groupId = repository.upsertGroup(name, 0, isDefault(options));
        repository.clearGroupParents(groupId);
        for (String parent : YamlMaps.getStringList(node, YamlMaps.GROUP_PARENT_LIST)) {
            repository.findGroupId(parent).ifPresent(parentId -> {
                try {
                    repository.setGroupParent(groupId, parentId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        repository.replaceGroupPermissions(groupId, null, YamlMaps.getStringList(node, YamlMaps.PERMISSIONS));
        Map<String, Object> worlds = YamlMaps.getSection(node, YamlMaps.WORLDS);
        if (worlds != null) {
            for (Map.Entry<String, Object> worldEntry : worlds.entrySet()) {
                if (worldEntry.getValue() instanceof Map<?, ?> worldNode) {
                    Map<String, Object> worldMap = cast(worldNode);
                    String contextKey = ContextKeyCodec.encodeLegacyWorld(worldEntry.getKey());
                    repository.replaceGroupPermissions(groupId, contextKey,
                            YamlMaps.getStringList(worldMap, YamlMaps.PERMISSIONS));
                    copyGroupOptions(repository, groupId, worldMap);
                }
            }
        }
        copyGroupOptions(repository, groupId, node);
        migrateLadder(repository, groupId, options);
    }

    private void migrateUser(LocalSqlRepository repository, String name, Map<String, Object> node) throws Exception {
        UUID userId = parseUserId(name);
        repository.upsertUser(userId, name, null, Instant.now());
        repository.clearUserGroups(userId, null);
        for (String parent : YamlMaps.getStringList(node, YamlMaps.USER_PARENT_LIST)) {
            repository.findGroupId(parent).ifPresent(groupId -> {
                try {
                    repository.setUserGroup(userId, groupId, null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
        repository.replaceUserPermissions(userId, null, YamlMaps.getStringList(node, YamlMaps.PERMISSIONS));
        Map<String, Object> worlds = YamlMaps.getSection(node, YamlMaps.WORLDS);
        if (worlds != null) {
            for (Map.Entry<String, Object> worldEntry : worlds.entrySet()) {
                if (worldEntry.getValue() instanceof Map<?, ?> worldNode) {
                    Map<String, Object> worldMap = cast(worldNode);
                    String contextKey = ContextKeyCodec.encodeLegacyWorld(worldEntry.getKey());
                    repository.replaceUserPermissions(userId, contextKey,
                            YamlMaps.getStringList(worldMap, YamlMaps.PERMISSIONS));
                }
            }
        }
        copyUserOptions(repository, userId, node);
    }

    private void migrateLadder(LocalSqlRepository repository, int groupId, Map<String, String> options) throws Exception {
        String ladderName = options.get("rank-ladder");
        String rank = options.get("rank");
        if (ladderName == null || rank == null) {
            return;
        }
        int ladderId = repository.upsertLadder(ladderName);
        repository.setLadderGroup(ladderId, groupId, Integer.parseInt(rank));
    }

    private static void copyGroupOptions(LocalSqlRepository repository,
                                         int groupId,
                                         Map<String, Object> node) throws Exception {
        Map<String, String> options = YamlMaps.collectLeafOptions(YamlMaps.optionsMap(node, null));
        for (Map.Entry<String, String> entry : options.entrySet()) {
            if ("rank".equals(entry.getKey()) || "rank-ladder".equals(entry.getKey()) || "default".equals(entry.getKey())) {
                continue;
            }
            repository.setGroupOption(groupId, entry.getKey(), entry.getValue());
        }
    }

    private static void copyUserOptions(LocalSqlRepository repository,
                                         UUID userId,
                                         Map<String, Object> node) throws Exception {
        Map<String, String> options = YamlMaps.collectLeafOptions(YamlMaps.optionsMap(node, null));
        for (Map.Entry<String, String> entry : options.entrySet()) {
            repository.setUserOption(userId, entry.getKey(), entry.getValue());
        }
    }

    private static boolean isDefault(Map<String, String> options) {
        String value = options.get("default");
        return value != null && Boolean.parseBoolean(value);
    }

    private static UUID parseUserId(String identifier) {
        try {
            return UUID.fromString(identifier);
        } catch (IllegalArgumentException ex) {
            return UUID.nameUUIDFromBytes(("OfflinePlayer:" + identifier).getBytes(StandardCharsets.UTF_8));
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> cast(Map<?, ?> map) {
        return (Map<String, Object>) map;
    }

    private static Map<String, Object> loadYaml(File yamlFile) throws IOException {
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(yamlFile.toPath())) {
            Object loaded = yaml.load(in);
            if (loaded instanceof Map<?, ?> map) {
                return cast(map);
            }
            return new LinkedHashMap<>();
        }
    }

    public record MigrationResult(boolean migrated, String message, File backupFile) {
        public static MigrationResult migrated(File backup) {
            return new MigrationResult(true, "Migrated YAML permissions to local SQL", backup);
        }

        public static MigrationResult skipped(String reason) {
            return new MigrationResult(false, reason, null);
        }
    }

    /**
     * Narrow adapter so {@link BackendDataTransfer} can target a SQL-backed backend during import.
     */
    public interface LocalSqlBackendAdapter {
        PermissionsGroupData getGroupData(String groupName);
        PermissionsUserData getUserData(String userName);
    }
}
