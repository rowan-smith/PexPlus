package dev.rono.permissions.core.api;

final class ModernWorlds {
    private ModernWorlds() {}

    static String toLegacy(String world) {
        return world == null || world.isEmpty() ? null : world;
    }
}
