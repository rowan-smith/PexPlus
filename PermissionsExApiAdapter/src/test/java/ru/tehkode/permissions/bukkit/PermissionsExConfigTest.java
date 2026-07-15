package ru.tehkode.permissions.bukkit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

public class PermissionsExConfigTest {

    @Test
    public void testConfigDefaults() {
        YamlConfiguration yaml = new YamlConfiguration();
        PermissionsExConfig config = new PermissionsExConfig(yaml, null);

        assertEquals("file", config.getDefaultBackend());
        assertFalse(config.isDebug());
        assertFalse(config.allowOps());
        assertFalse(config.createUserRecords());
        assertEquals("plugins/PermissionsEx", config.getBasedir());
    }

    @Test
    public void testConfigOverrides() {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("permissions.backend", "sql");
        yaml.set("permissions.debug", true);
        yaml.set("permissions.basedir", "custom/dir");

        PermissionsExConfig config = new PermissionsExConfig(yaml, null);

        assertEquals("sql", config.getDefaultBackend());
        assertTrue(config.isDebug());
        assertEquals("custom/dir", config.getBasedir());
    }
}
