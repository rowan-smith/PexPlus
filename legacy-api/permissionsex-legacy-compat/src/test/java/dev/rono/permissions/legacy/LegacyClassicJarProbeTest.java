package dev.rono.permissions.legacy;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Optional regression probe: drop classic third-party plugin JARs into
 * {@code src/test/resources/plugin-jars/} to verify they still resolve {@link PermissionsEx} static entry points.
 */
class LegacyClassicJarProbeTest {

    @Test
    void optionalClassicPluginJarsResolveLegacyEntryPoints() throws Exception {
        var dir = Path.of("src/test/resources/plugin-jars");

        try (var jars = Files.list(dir).filter(p -> p.toString().endsWith(".jar"))) {
            var files = jars.toArray(Path[]::new);
            Assumptions.assumeTrue(files.length > 0, "plugin-jars directory is empty");

            for (var jar : files) {
                probeJar(jar);
            }
        }
    }

    private static void probeJar(Path jar) throws Exception {
        var parent = LegacyClassicJarProbeTest.class.getClassLoader();
        assertLegacyEntryPoints(parent);

        try (var loader = new URLClassLoader(new URL[]{jar.toUri().toURL()}, parent);
             var jarStream = new JarInputStream(Files.newInputStream(jar))) {
            JarEntry entry;

            while ((entry = jarStream.getNextJarEntry()) != null) {
                if (!entry.getName().endsWith(".class")) {
                    continue;
                }

                var classBytes = jarStream.readAllBytes();
                if (!referencesTehkode(classBytes)) {
                    continue;
                }

                var className = entry.getName().substring(0, entry.getName().length() - 6).replace('/', '.');
                Class.forName(className, true, loader);
            }
        }
    }

    private static void assertLegacyEntryPoints(ClassLoader loader) throws ReflectiveOperationException {
        var permissionsEx = Class.forName("ru.tehkode.permissions.bukkit.PermissionsEx", false, loader);

        var ignore = permissionsEx.getMethod("getPermissionManager");
        var ignore2 = permissionsEx.getMethod("isAvailable");
    }

    private static boolean referencesTehkode(byte[] classBytes) {
        return new String(classBytes, StandardCharsets.ISO_8859_1).contains("ru/tehkode");
    }
}
