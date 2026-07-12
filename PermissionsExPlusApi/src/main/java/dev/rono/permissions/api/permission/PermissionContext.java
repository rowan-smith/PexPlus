package dev.rono.permissions.api.permission;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface PermissionContext {

    String SERVER = "server";
    String WORLD = "world";
    String DIMENSION = "dimension";
    String REGION = "region";
    String GAMEMODE = "gamemode";
    String STATE = "state";

    Map<String, String> attributes();

    default Optional<String> get(String key) {
        return Optional.ofNullable(attributes().get(key)).filter(v -> !v.isEmpty());
    }

    static PermissionContext global() {
        return PermissionContextImpl.EMPTY;
    }

    static PermissionContext of(Map<String, String> attributes) {
        return PermissionContextImpl.of(attributes);
    }

    static PermissionContext server(String server) {
        if (server == null || server.isEmpty()) {
            return global();
        }
        return of(Map.of(SERVER, server));
    }

    static PermissionContext world(String world) {
        if (world == null || world.isEmpty()) {
            return global();
        }
        return of(Map.of(WORLD, world));
    }

    static PermissionContext world(String server, String world) {
        return of(Map.of(SERVER, server, WORLD, world));
    }

    static PermissionContext of(String world, String server, String region, String gamemode) {
        Map<String, String> attrs = new HashMap<>(4);
        putIfPresent(attrs, WORLD, world);
        putIfPresent(attrs, SERVER, server);
        putIfPresent(attrs, REGION, region);
        putIfPresent(attrs, GAMEMODE, gamemode);
        return of(attrs);
    }

    static PermissionContext withState(String world, String state) {
        Map<String, String> attrs = new HashMap<>(2);
        putIfPresent(attrs, WORLD, world);
        putIfPresent(attrs, STATE, state);
        return of(attrs);
    }

    static PermissionContext fromMap(Map<String, String> legacy) {
        return of(legacy);
    }

    default Map<String, String> toMap() {
        return attributes();
    }

    default boolean isGlobal() {
        return attributes().isEmpty();
    }

    private static void putIfPresent(Map<String, String> attrs, String key, String value) {
        if (value != null && !value.isEmpty()) {
            attrs.put(key, value);
        }
    }

}
