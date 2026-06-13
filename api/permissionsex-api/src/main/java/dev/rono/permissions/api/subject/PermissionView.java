package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.world.Worlds;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Read-only permission and metadata evaluation for a subject.
 *
 * <p>Internal role interface — composed by {@link PermissionSubject}. All evaluation must delegate to
 * the engine ({@code PermissionEntity} / hierarchy traverser); this layer must not duplicate resolution
 * logic.</p>
 *
 * <p>{@link SubjectWorldContext} is the world-bound projection of these read operations.</p>
 */
public interface PermissionView {

    /**
     * Checks whether this subject effectively holds the given permission in the specified world.
     *
     * @param permission permission node to check
     * @param world      world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return {@code true} if the permission is granted, {@code false} otherwise
     */
    boolean has(String permission, String world);

    /**
     * Alias for {@link #has(String, String)}.
     *
     * @param permission permission node to check
     * @param world      world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return {@code true} if the permission is granted, {@code false} otherwise
     */
    default boolean hasPermission(String permission, String world) {
        return has(permission, world);
    }

    /**
     * Returns direct permission assignments in the given world (not inherited).
     *
     * @param world world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return unmodifiable list of own permission expressions
     */
    List<String> permissions(String world);

    /**
     * Returns effective permissions after inheritance in the given world.
     *
     * @param world world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return unmodifiable list of effective permission expressions
     */
    List<String> effectivePermissions(String world);

    /**
     * Returns permission nodes with active timed grants in the given world.
     *
     * @param world world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return list of timed permission nodes
     */
    List<String> timedPermissions(String world);

    /**
     * Returns timed permission nodes with remaining lifetime metadata in the given world.
     *
     * @param world world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return list of timed permission entries
     */
    List<TimedPermissionEntry> timedPermissionEntries(String world);

    /**
     * Returns seconds remaining on a timed permission grant in the given world.
     *
     * @param permission timed permission node
     * @param world      world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return seconds until expiry; {@code 0} if the timed permission is absent
     */
    int timedPermissionRemainingSeconds(String permission, String world);

    /**
     * Returns whether the permission is directly assigned as a timed grant in the given world.
     *
     * <p>Unlike {@link #has(String, String)}, this checks only the timed-permission list.</p>
     *
     * @param permission permission node to look up
     * @param world      world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return {@code true} if a timed grant exists for the node, {@code false} otherwise
     */
    default boolean hasTimedPermission(String permission, String world) {
        return timedPermissions(world).contains(permission);
    }

    /**
     * Returns worlds where this subject has permissions, options, or inheritance data.
     *
     * @return set of configured world names (global is represented by {@link Worlds#GLOBAL})
     */
    Set<String> configuredWorlds();

    /**
     * Returns direct permissions keyed by world.
     *
     * @return map of world to own permission lists
     */
    Map<String, List<String>> permissionsByWorld();

    /**
     * Returns effective permissions keyed by world.
     *
     * @return map of world to effective permission lists
     */
    Map<String, List<String>> effectivePermissionsByWorld();

    /**
     * Returns the effective chat prefix for the given world, including inheritance.
     *
     * @param world world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return resolved prefix, or empty string when none is defined
     */
    String prefix(String world);

    /**
     * Returns the effective chat suffix for the given world, including inheritance.
     *
     * @param world world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return resolved suffix, or empty string when none is defined
     */
    String suffix(String world);

    /**
     * Returns an effective option value for the given world, including inheritance.
     *
     * @param key   option key
     * @param world world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return option value, or {@code null} if unset
     */
    String option(String key, String world);

    /**
     * Returns effective options for the given world, including inheritance.
     *
     * @param world world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return map of option keys to resolved values
     */
    Map<String, String> options(String world);

    /**
     * Returns timed permission entries across every configured world, including global.
     *
     * @return aggregated timed permission entries from all worlds with data
     */
    default List<TimedPermissionEntry> allTimedPermissionEntries() {
        java.util.LinkedHashSet<String> worlds = new java.util.LinkedHashSet<>();
        worlds.add(Worlds.GLOBAL);
        worlds.addAll(configuredWorlds());
        return worlds.stream().flatMap(world -> timedPermissionEntries(world).stream()).toList();
    }
}
