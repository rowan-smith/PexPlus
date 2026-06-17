package dev.rono.permissions.core.config;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PexConfigDataLegacyBackendTest {

    @Test
    void normalizesActiveFileBackendToH2WithMigrationSource() {
        LinkedHashMap<String, Object> fileSection = new LinkedHashMap<>();
        fileSection.put(PexConfigData.KEY_BACKEND_TYPE, "file");
        fileSection.put(PexConfigData.KEY_BACKEND_FILE_LEAF, "customperms.yml");

        Map<String, Object> root = new LinkedHashMap<>();
        root.put(PexConfigData.KEY_BACKEND, "file");
        LinkedHashMap<String, Object> backends = new LinkedHashMap<>();
        backends.put("file", fileSection);
        root.put(PexConfigData.KEY_BACKENDS, backends);

        PexConfigData data = PexConfigData.fromPermissionsMap(root, () -> ".", PexConfigFlavor.SPIGOT);

        assertEquals("h2", data.backend());
        assertEquals("customperms.yml", data.storeRelative());
        assertEquals("customperms.yml", data.backends().get("h2").get("migration-source"));
    }

    @Test
    void normalizesLegacyLocalBackendAliasToH2() {
        LinkedHashMap<String, Object> localSection = new LinkedHashMap<>();
        localSection.put(PexConfigData.KEY_BACKEND_TYPE, "local");
        localSection.put(PexConfigData.KEY_DATABASE, "permissions");

        Map<String, Object> root = new LinkedHashMap<>();
        root.put(PexConfigData.KEY_BACKEND, "local");
        LinkedHashMap<String, Object> backends = new LinkedHashMap<>();
        backends.put("local", localSection);
        root.put(PexConfigData.KEY_BACKENDS, backends);

        PexConfigData data = PexConfigData.fromPermissionsMap(root, () -> ".", PexConfigFlavor.SPIGOT);

        assertEquals("h2", data.backend());
        assertEquals("h2", data.backends().get("h2").get("type"));
        assertEquals("permissions", data.backends().get("h2").get("database"));
        assertFalse(data.backends().containsKey("local"));
    }
}
