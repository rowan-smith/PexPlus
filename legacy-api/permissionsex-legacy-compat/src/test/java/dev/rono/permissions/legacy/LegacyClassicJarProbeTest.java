package dev.rono.permissions.legacy;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * Optional regression probe: drop classic third-party plugin JARs into
 * {@code src/test/resources/plugin-jars/} to verify they still link against legacy PermissionsEx entry points.
 *
 * <p>Hook plugins reference {@code ru.tehkode.permissions.bukkit.PermissionsEx} at compile time (provided scope).
 * Vault ships {@code Permission_PermissionsEx} and expects PermissionsEx on the server classpath at runtime.</p>
 */
class LegacyClassicJarProbeTest {

    private static final String VAULT_PEX_BRIDGE =
            "net.milkbowl.vault.permission.plugins.Permission_PermissionsEx";
    private static final String LEGACY_PERMISSIONS_EX = "ru.tehkode.permissions.bukkit.PermissionsEx";
    private static final byte[] LEGACY_PEX_MARKER = "ru/tehkode/permissions".getBytes(java.nio.charset.StandardCharsets.UTF_8);

    @Test
    void optionalClassicPluginJarsResolveLegacyEntryPoints() throws Exception {
        Path dir = Path.of("src/test/resources/plugin-jars");
        if (!Files.isDirectory(dir)) {
            Assumptions.assumeTrue(false, "No plugin-jars directory — add classic hook plugin JARs to enable this probe");
        }
        try (Stream<Path> jars = Files.list(dir).filter(p -> p.toString().endsWith(".jar"))) {
            Path[] files = jars.toArray(Path[]::new);
            Assumptions.assumeTrue(files.length > 0, "plugin-jars directory is empty");
            for (Path jar : files) {
                probeJar(jar);
            }
        }
    }

    private static void probeJar(Path jar) throws Exception {
        ClassLoader parent = LegacyClassicJarProbeTest.class.getClassLoader();
        Class<?> permissionsEx = Class.forName(LEGACY_PERMISSIONS_EX, false, parent);
        permissionsEx.getMethod("getPermissionManager");
        permissionsEx.getMethod("getApi");
        permissionsEx.getMethod("isAvailable");

        if (!jarReferencesLegacyPex(jar)) {
            Assumptions.assumeTrue(
                    false,
                    "Skipping " + jar.getFileName() + " — no ru.tehkode.permissions references in bytecode");
        }

        try (URLClassLoader loader = new URLClassLoader(new URL[] {jar.toUri().toURL()}, parent)) {
            if (jarContainsEntry(jar, VAULT_PEX_BRIDGE.replace('.', '/') + ".class")) {
                Class.forName(VAULT_PEX_BRIDGE, false, loader);
                return;
            }
            if (jarContainsEntry(jar, LEGACY_PERMISSIONS_EX.replace('.', '/') + ".class")) {
                Class.forName(LEGACY_PERMISSIONS_EX, false, loader);
            }
        }
    }

    private static boolean jarContainsEntry(Path jar, String entryName) throws IOException {
        try (JarFile jarFile = new JarFile(jar.toFile())) {
            return jarFile.getEntry(entryName) != null;
        }
    }

    private static boolean jarReferencesLegacyPex(Path jar) throws IOException {
        try (JarFile jarFile = new JarFile(jar.toFile())) {
            for (JarEntry entry : jarFile.stream().toList()) {
                if (!entry.getName().endsWith(".class")) {
                    continue;
                }
                try (InputStream in = jarFile.getInputStream(entry)) {
                    if (containsMarker(in.readAllBytes())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean containsMarker(byte[] data) {
        if (data.length < LEGACY_PEX_MARKER.length) {
            return false;
        }
        for (int i = 0; i <= data.length - LEGACY_PEX_MARKER.length; i++) {
            boolean match = true;
            for (int j = 0; j < LEGACY_PEX_MARKER.length; j++) {
                if (data[i + j] != LEGACY_PEX_MARKER[j]) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return true;
            }
        }
        return false;
    }
}
