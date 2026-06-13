package dev.rono.permissions.api.permission;

import java.util.HashMap;
import java.util.Map;

/**
 * Standard keys for permission check and grant context maps.
 *
 * <p>Example:</p>
 * <pre>{@code
 * Map<String, String> context = PermissionContext.of(
 *     "survival", "lobby-1", "spawn", "creative");
 * manager.hasPermission(holder, "my.node", context);
 * }</pre>
 */
public final class PermissionContext {

    /** Realm/world scope for permission resolution. */
    public static final String WORLD = "world";

    /** Server scope (proxy realms); used when {@link #WORLD} is absent. */
    public static final String SERVER = "server";

    /** Optional region scope (for plugins that interpret context). */
    public static final String REGION = "region";

    /** Optional gamemode scope (for plugins that interpret context). */
    public static final String GAMEMODE = "gamemode";

    /** Optional state scope (for example {@code "event"} during minigames). */
    public static final String STATE = "state";

    private PermissionContext() {}

  /**
   * Builds a context with the common world/server/region/gamemode keys.
   *
   * @param world    realm or world name
   * @param server   server name (proxy) or logical server id
   * @param region   region name
   * @param gamemode gamemode name
   * @return immutable context map
   */
    public static Map<String, String> of(String world, String server, String region, String gamemode) {
        Map<String, String> context = new HashMap<>(4);
        putIfPresent(context, WORLD, world);
        putIfPresent(context, SERVER, server);
        putIfPresent(context, REGION, region);
        putIfPresent(context, GAMEMODE, gamemode);
        return Map.copyOf(context);
    }

    /**
     * Builds a context with world and optional {@link #STATE}.
     *
     * @param world world or realm name
     * @param state optional state value (for example {@code "event"})
     * @return immutable context map
     */
    public static Map<String, String> withState(String world, String state) {
        Map<String, String> context = new HashMap<>(2);
        putIfPresent(context, WORLD, world);
        putIfPresent(context, STATE, state);
        return Map.copyOf(context);
    }

    private static void putIfPresent(Map<String, String> context, String key, String value) {
        if (value != null && !value.isEmpty()) {
            context.put(key, value);
        }
    }
}
