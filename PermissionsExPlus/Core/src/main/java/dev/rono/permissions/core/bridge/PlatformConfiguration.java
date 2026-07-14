package dev.rono.permissions.core.bridge;

import java.nio.file.Path;

public interface PlatformConfiguration {
    Path dataDirectory();

    void saveResource(String resource, boolean replace);

    default Path resolve(String path) {
        return dataDirectory().resolve(path);
    }
}