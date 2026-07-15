package dev.rono.permissions.core.config;

import dev.rono.permissions.core.platform.PlatformConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationTest {
    @TempDir
    Path directory;

    @Test
    void loadsGeneralConfigurationIncludingNestedWildcardSetting() throws IOException {
        Files.writeString(directory.resolve("config.yml"), """
                default-group: member
                case-sensitive: true
                wildcards:
                  enabled: false
                  allow-negations: false # explanatory comment
                  enable-shorthand-expansions: false
                debug:
                  verbose: true
                hooks:
                  vault:
                    enabled: false
                  placeholder-api: false
                """);

        var configuration = GeneralConfiguration.load(platform());

        assertEquals("member", configuration.defaultGroup());
        assertTrue(configuration.caseSensitive());
        assertFalse(configuration.wildcardsEnabled());
        assertFalse(configuration.allowNegations());
        assertFalse(configuration.shorthandExpansionsEnabled());
        assertTrue(configuration.verboseDebug());
        assertFalse(configuration.vaultEnabled());
        assertFalse(configuration.placeholderApiEnabled());
    }

    @Test
    void loadsAdvancedConfigurationAndEnvironmentValues() throws IOException {
        Files.writeString(directory.resolve("advanced.yml"), """
                messaging:
                  type: redis
                  redis:
                    host: redis.internal
                    port: 6380
                    password: secret
                    channel: permissions
                    timeout: 2500
                cache:
                  offline-eviction-time: 17
                  max-cached-offline-users: 25
                  preload-on-join: false
                  cache-failure-fallback: allow
                  auto-save-interval: 9
                  log-save: true
                  log-cache-eviction: true
                  log-cache-mode: individual
                temporary-permissions:
                  expiry-check-interval: 11
                  log-expiry: true
                  log-expiry-mode: individual
                identity:
                  uuid-source: online_only
                server-context:
                  global-name: lobby
                  environment:
                    region: au
                inheritance:
                  max-depth: 3
                  conflict-resolution: true_wins
                  meta-formatting: accumulated
                threading:
                  worker-pool-size: 7
                commands:
                  register-base-commands: false
                audit-log:
                  log-to-file: false
                  broadcast-to-ops: false
                  network-wide-logging: false
                """);

        var configuration = AdvancedConfiguration.load(platform());

        assertEquals("redis", configuration.messagingType());
        assertEquals(17, configuration.offlineEvictionTime());
        assertEquals(25, configuration.maxCachedOfflineUsers());
        assertFalse(configuration.preloadOnJoin());
        assertEquals(CacheFailureFallback.ALLOW, configuration.cacheFailureFallback());
        assertEquals(11, configuration.expiryCheckInterval());
        assertTrue(configuration.logExpiry());
        assertEquals(ExpiryLogMode.INDIVIDUAL, configuration.logExpiryMode());
        assertEquals("lobby", configuration.globalContextName());
        assertEquals("au", configuration.environment().get("region"));
        assertEquals(3, configuration.maxInheritanceDepth());
        assertEquals(PermissionConflictResolution.TRUE_WINS, configuration.conflictResolution());
        assertEquals(MetaFormatting.ACCUMULATED, configuration.metaFormatting());
        assertEquals("redis.internal", configuration.redisHost());
        assertEquals(6380, configuration.redisPort());
        assertEquals("permissions", configuration.redisChannel());
        assertFalse(configuration.auditLogToFile());
        assertEquals(9, configuration.autoSaveInterval());
        assertTrue(configuration.logSave());
        assertTrue(configuration.logCacheEviction());
        assertEquals(CacheLogMode.INDIVIDUAL, configuration.logCacheMode());
        assertEquals(7, configuration.workerPoolSize());
        assertFalse(configuration.registerBaseCommands());
        assertFalse(configuration.broadcastToOps());
        assertFalse(configuration.networkWideLogging());
    }

    @Test
    void cacheAnnouncementsDefaultToDisabled() throws IOException {
        Files.writeString(directory.resolve("advanced.yml"), "advanced-version: 1\n");

        var configuration = AdvancedConfiguration.load(platform());

        assertFalse(configuration.logSave());
        assertFalse(configuration.logCacheEviction());
        assertEquals(CacheLogMode.TOTAL, configuration.logCacheMode());
        assertEquals(5000, configuration.maxCachedOfflineUsers());
        assertTrue(configuration.preloadOnJoin());
        assertEquals(CacheFailureFallback.DENY, configuration.cacheFailureFallback());
        assertFalse(configuration.logExpiry());
        assertEquals(ExpiryLogMode.TOTAL, configuration.logExpiryMode());
        assertEquals(PermissionConflictResolution.DENY_WINS, configuration.conflictResolution());
        assertEquals(MetaFormatting.HIGHEST_WEIGHT, configuration.metaFormatting());
        assertEquals(4, configuration.workerPoolSize());
        assertTrue(configuration.registerBaseCommands());
    }

    @Test
    void rejectsInvalidProductionControlValues() throws IOException {
        Files.writeString(directory.resolve("advanced.yml"), """
                threading:
                  worker-pool-size: 0
                """);

        assertThrows(IllegalStateException.class, () -> AdvancedConfiguration.load(platform()));

        Files.writeString(directory.resolve("advanced.yml"), """
                inheritance:
                  conflict-resolution: random
                """);

        assertThrows(IllegalStateException.class, () -> AdvancedConfiguration.load(platform()));

        Files.writeString(directory.resolve("advanced.yml"), """
                inheritance:
                  meta-formatting: random
                """);

        assertThrows(IllegalStateException.class, () -> AdvancedConfiguration.load(platform()));

        Files.writeString(directory.resolve("advanced.yml"), """
                temporary-permissions:
                  log-expiry-mode: random
                """);

        assertThrows(IllegalStateException.class, () -> AdvancedConfiguration.load(platform()));

        Files.writeString(directory.resolve("advanced.yml"), """
                temporary-permissions:
                  expiry-check-interval: 0
                """);

        assertThrows(IllegalStateException.class, () -> AdvancedConfiguration.load(platform()));
    }

    @Test
    void requestsBundledResourceWhenConfigurationIsMissing() {
        var saved = new java.util.ArrayList<String>();

        var platform = new PlatformConfiguration() {
            @Override
            public Path dataDirectory() {
                return directory;
            }

            @Override
            public void saveResource(String resource, boolean replace) {
                saved.add(resource);

                try {
                    Files.writeString(resolve(resource), "default-group: guest");
                } catch (IOException exception) {
                    throw new AssertionError(exception);
                }
            }
        };

        assertEquals("guest", GeneralConfiguration.load(platform).defaultGroup());
        assertEquals(List.of("config.yml"), saved);
    }

    @Test
    void loadsDatabaseBackendConfigurationAndRejectsMissingRequiredValues() throws IOException {
        var valid = """
                type: postgresql
                local:
                  storage-directory: storage/sql
                  filename: permissions.db
                credentials:
                  host: db.internal
                  port: 5432 # default PostgreSQL port
                  database: permissions
                  username: pex
                  password: secret
                pool:
                  maximum-pool-size: 12
                  minimum-idle: 3
                  connection-timeout: 4321
                  max-lifetime: 60000
                hibernate:
                  ddl-generation: validate
                """;

        Files.writeString(directory.resolve("database.yml"), valid);

        var backend = BackendConfiguration.load(platform());

        assertEquals(BackendType.POSTGRES, backend.type());
        assertEquals("storage/sql", backend.localStorageDirectory());
        assertEquals("permissions.db", backend.localFilename());
        assertEquals(directory.resolve("storage/sql").toAbsolutePath(), backend.resolveLocalStorageDirectory(directory));
        assertEquals(directory.resolve("storage/sql/permissions.db").toAbsolutePath(), backend.resolveLocalFile(directory));
        assertEquals("db.internal", backend.credentials().host());
        assertEquals(5432, backend.credentials().port());
        assertEquals(12, backend.pool().maximumPoolSize());
        assertEquals(4321L, backend.pool().connectionTimeout());
        assertEquals(DdlGeneration.VALIDATE, backend.ddlGeneration());

        Files.writeString(directory.resolve("database.yml"), valid.replace("4321", "249"));

        assertThrows(IllegalStateException.class, () -> BackendConfiguration.load(platform()));

        Files.writeString(directory.resolve("database.yml"), valid.replace("validate", "create-drop"));

        assertThrows(IllegalStateException.class, () -> BackendConfiguration.load(platform()));

        Files.writeString(directory.resolve("database.yml"), valid.replace("storage/sql", "../outside"));

        assertThrows(IllegalStateException.class, () -> BackendConfiguration.load(platform()));

        Files.writeString(directory.resolve("database.yml"), "type: memory\n");

        assertThrows(IllegalStateException.class, () -> BackendConfiguration.load(platform()));
    }

    @Test
    void parsesBackendAliasesAndExposesPersistenceFlags() {
        assertEquals(BackendType.POSTGRES, BackendType.parse("postgresql"));
        assertEquals(BackendType.YAML, BackendType.parse("yml"));
        assertEquals(DdlGeneration.UPDATE, DdlGeneration.parse("update"));
        assertEquals(DdlGeneration.NONE, DdlGeneration.parse("NONE"));
        assertFalse(BackendType.MEMORY.persistent());
        assertTrue(BackendType.SQLITE.persistent());
        assertThrows(IllegalArgumentException.class, () -> BackendType.parse("mongodb"));
    }

    @Test
    void bundledConfigurationFilesMatchTheTypedConfigurationModels() throws IOException {
        for (var resource : java.util.List.of("config.yml", "advanced.yml", "database.yml")) {
            try (var input = ConfigurationTest.class.getClassLoader().getResourceAsStream(resource)) {
                assertNotNull(input, resource);

                Files.copy(input, directory.resolve(resource), StandardCopyOption.REPLACE_EXISTING);
            }
        }

        var general = GeneralConfiguration.load(platform());
        var advanced = AdvancedConfiguration.load(platform());
        var backend = BackendConfiguration.load(platform());

        assertEquals("default", general.defaultGroup());
        assertTrue(general.shorthandExpansionsEnabled());
        assertEquals("mix_mode", advanced.uuidSource());
        assertEquals("localhost", advanced.redisHost());
        assertEquals(BackendType.H2, backend.type());
        assertEquals("data", backend.localStorageDirectory());
        assertEquals(5_000L, backend.pool().connectionTimeout());
        assertEquals(DdlGeneration.UPDATE, backend.ddlGeneration());
    }

    @Test
    void rejectsConfigurationVersionsTheRuntimeCannotInterpret() throws IOException {
        Files.writeString(directory.resolve("config.yml"), "config-version: 2\n");
        Files.writeString(directory.resolve("advanced.yml"), "advanced-version: 2\n");
        Files.writeString(directory.resolve("database.yml"), "data-version: 2\n");

        assertThrows(IllegalStateException.class, () -> GeneralConfiguration.load(platform()));
        assertThrows(IllegalStateException.class, () -> AdvancedConfiguration.load(platform()));
        assertThrows(IllegalStateException.class, () -> BackendConfiguration.load(platform()));
    }

    private PlatformConfiguration platform() {
        return new PlatformConfiguration() {
            @Override
            public Path dataDirectory() {
                return directory;
            }

            @Override
            public void saveResource(String resource, boolean replace) {
                throw new AssertionError("resource should exist");
            }
        };
    }
}
