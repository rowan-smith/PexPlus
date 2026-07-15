package dev.rono.permissions.core.store;

import dev.rono.permissions.core.config.DatabasePool;
import dev.rono.permissions.core.config.DdlGeneration;
import dev.rono.permissions.core.store.dto.ContextDto;
import dev.rono.permissions.core.store.dto.GroupDto;
import dev.rono.permissions.core.store.dto.LadderDto;
import dev.rono.permissions.core.store.dto.LadderGroupDto;
import dev.rono.permissions.core.store.dto.OptionDto;
import dev.rono.permissions.core.store.dto.ParentDto;
import dev.rono.permissions.core.store.dto.PermissionDto;
import dev.rono.permissions.core.store.dto.UserDto;
import dev.rono.permissions.core.store.repository.GroupRepository;
import dev.rono.permissions.core.store.repository.LadderRepository;
import dev.rono.permissions.core.store.repository.NodeRepository;
import dev.rono.permissions.core.store.repository.UserRepository;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

public final class HibernateDataStore implements DataStore {
    private final String name, url, driver, username, password;
    private final DatabasePool pool;
    private final DdlGeneration ddlGeneration;
    private final boolean persistent;
    private final NodeRepository nodes = new NodeRepository();
    private final UserRepository users = new UserRepository(nodes);
    private final GroupRepository groups = new GroupRepository(nodes);
    private final LadderRepository ladders = new LadderRepository();
    private SessionFactory factory;

    public HibernateDataStore(String name, String url, String driver, String username, String password, DatabasePool pool, boolean persistent) {
        this(name, url, driver, username, password, pool, DdlGeneration.UPDATE, persistent);
    }

    public HibernateDataStore(String name, String url, String driver, String username, String password, DatabasePool pool, DdlGeneration ddlGeneration, boolean persistent) {
        this.name = name;
        this.url = url;
        this.driver = driver;
        this.username = username;
        this.password = password;
        this.pool = pool;
        this.ddlGeneration = ddlGeneration;
        this.persistent = persistent;
    }

    @Override
    public synchronized void open() {
        if (factory != null) {
            return;
        }

        PersistenceLogging.suppressRoutineMessages();

        var config = new Configuration().addAnnotatedClass(UserDto.class).addAnnotatedClass(GroupDto.class)
                .addAnnotatedClass(LadderDto.class)
                .addAnnotatedClass(PermissionDto.class).addAnnotatedClass(OptionDto.class)
                .addAnnotatedClass(ParentDto.class).addAnnotatedClass(ContextDto.class)
                .addAnnotatedClass(LadderGroupDto.class);
        config.setProperty(AvailableSettings.JAKARTA_JDBC_URL, url);
        config.setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, driver);

        if (username != null && !username.isBlank()) {
            config.setProperty(AvailableSettings.JAKARTA_JDBC_USER, username);
        }

        if (password != null && !password.isBlank()) {
            config.setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, password);
        }

        config.setProperty(AvailableSettings.HBM2DDL_AUTO, ddlGeneration.hibernateValue());
        config.setProperty(AvailableSettings.SHOW_SQL, "false");
        config.setProperty("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
        config.setProperty("hibernate.hikari.maximumPoolSize", Integer.toString(pool.maximumPoolSize()));
        config.setProperty("hibernate.hikari.minimumIdle", Integer.toString(pool.minimumIdle()));
        config.setProperty("hibernate.hikari.connectionTimeout", Long.toString(pool.connectionTimeout()));
        config.setProperty("hibernate.hikari.maxLifetime", Long.toString(pool.maxLifetime()));

        factory = config.buildSessionFactory();
    }

    @Override
    public Optional<String> get(String category, String key) {
        try (var session = session()) {
            return switch (category) {
                case "users" -> users.find(session, key).map(SnapshotCodec::user);
                case "groups" -> groups.find(session, key).map(SnapshotCodec::group);
                case "ladders" -> ladders.find(session, key).map(SnapshotCodec::ladder);
                default -> throw unknownCategory(category);
            };
        }
    }

    @Override
    public Map<String, String> all(String category) {
        try (var session = session()) {
            var result = new LinkedHashMap<String, String>();

            switch (category) {
                case "users" -> users.all(session).forEach((key, value) -> result.put(key, SnapshotCodec.user(value)));
                case "groups" -> groups.all(session).forEach((key, value) -> result.put(key, SnapshotCodec.group(value)));
                case "ladders" -> ladders.all(session).forEach((key, value) -> result.put(key, SnapshotCodec.ladder(value)));
                default -> throw unknownCategory(category);
            }

            return Map.copyOf(result);
        }
    }

    @Override
    public void put(String category, String key, String payload) {
        try (var session = session()) {
            var tx = session.beginTransaction();

            try {
                save(session, category, payload);

                tx.commit();
            } catch (RuntimeException error) {
                if (tx.isActive()) {
                    tx.rollback();
                }

                throw error;
            }
        }
    }

    @Override
    public boolean remove(String category, String key) {
        try (var session = session()) {
            var tx = session.beginTransaction();

            try {
                var changed = switch (category) {
                    case "users" -> users.delete(session, key);
                    case "groups" -> groups.delete(session, key);
                    case "ladders" -> ladders.delete(session, key);
                    default -> throw unknownCategory(category);
                };

                tx.commit();

                return changed;
            } catch (RuntimeException error) {
                if (tx.isActive()) {
                    tx.rollback();
                }

                throw error;
            }
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean persistent() {
        return persistent;
    }

    @Override
    public synchronized void close() {
        if (factory != null) {
            factory.close();
            factory = null;
        }
    }

    private Session session() {
        if (factory == null) {
            throw new IllegalStateException("Storage is not open");
        }

        return factory.openSession();
    }

    private void save(Session session, String category, String payload) {
        switch (category) {
            case "users" -> users.save(session, SnapshotCodec.user(payload));
            case "groups" -> groups.save(session, SnapshotCodec.group(payload));
            case "ladders" -> ladders.save(session, SnapshotCodec.ladder(payload));
            default -> throw unknownCategory(category);
        }
    }

    private IllegalArgumentException unknownCategory(String category) {
        return new IllegalArgumentException("Unsupported relational storage category: " + category);
    }
}
