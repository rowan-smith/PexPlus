package dev.rono.permissions.api.world;

import dev.rono.permissions.api.realm.Realms;

/**
 * World context helpers for the modern API.
 *
 * @deprecated Use {@link Realms} — {@code Worlds} is the legacy name for realm namespace helpers.
 */
@Deprecated(since = "3.0.0")
public final class Worlds {
    private Worlds() {}

    /** @deprecated Use {@link Realms#GLOBAL} */
    @Deprecated(since = "3.0.0")
    public static final String GLOBAL = Realms.GLOBAL;

    /** @deprecated Use {@link Realms#isGlobal(String)} */
    @Deprecated(since = "3.0.0")
    public static boolean isGlobal(String world) {
        return Realms.isGlobal(world);
    }

    /** @deprecated Use {@link Realms#normalize(String)} */
    @Deprecated(since = "3.0.0")
    public static String normalize(String world) {
        return Realms.normalize(world);
    }

    /** @deprecated Use {@link Realms#mapKey(String)} */
    @Deprecated(since = "3.0.0")
    public static String mapKey(String world) {
        return Realms.mapKey(world);
    }

    /** @deprecated Use {@link Realms#fromMapKey(String)} */
    @Deprecated(since = "3.0.0")
    public static String fromMapKey(String key) {
        return Realms.fromMapKey(key);
    }
}
