package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.permission.PermissionContext;
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
 * <p>{@link SubjectContext} is the context-bound projection of these read operations.</p>
 */
public interface PermissionView {

    /**
     * Checks whether this subject effectively holds the given permission in the specified context.
     *
     * @param permission permission node to check
     * @param context    permission scope
     * @return {@code true} if the permission is granted, {@code false} otherwise
     */
    boolean has(String permission, PermissionContext context);

    /**
     * Checks whether this subject effectively holds the given permission in the specified world.
     *
     * @param permission permission node to check
     * @param world      world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return {@code true} if the permission is granted, {@code false} otherwise
     */
    default boolean has(String permission, String world) {
        return has(permission, legacyWorldContext(world));
    }

    /**
     * Alias for {@link #has(String, PermissionContext)}.
     *
     * @param permission permission node to check
     * @param context    permission scope
     * @return {@code true} if the permission is granted, {@code false} otherwise
     */
    default boolean hasPermission(String permission, PermissionContext context) {
        return has(permission, context);
    }

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

    /** @return direct permission assignments in {@code context} (not inherited) */
    List<String> permissions(PermissionContext context);

    /** @return direct permission assignments in the given world (not inherited) */
    default List<String> permissions(String world) {
        return permissions(legacyWorldContext(world));
    }

    /** @return effective permissions after inheritance in {@code context} */
    List<String> effectivePermissions(PermissionContext context);

    /** @return effective permissions after inheritance in the given world */
    default List<String> effectivePermissions(String world) {
        return effectivePermissions(legacyWorldContext(world));
    }

    /** @return permission nodes with active timed grants in {@code context} */
    List<String> timedPermissions(PermissionContext context);

    default List<String> timedPermissions(String world) {
        return timedPermissions(legacyWorldContext(world));
    }

    /** @return timed permission entries with remaining lifetime metadata in {@code context} */
    List<TimedPermissionEntry> timedPermissionEntries(PermissionContext context);

    default List<TimedPermissionEntry> timedPermissionEntries(String world) {
        return timedPermissionEntries(legacyWorldContext(world));
    }

    /** @return seconds remaining on a timed permission grant in {@code context} */
    int timedPermissionRemainingSeconds(String permission, PermissionContext context);

    default int timedPermissionRemainingSeconds(String permission, String world) {
        return timedPermissionRemainingSeconds(permission, legacyWorldContext(world));
    }

    /** @return whether the permission is directly assigned as a timed grant in {@code context} */
    default boolean hasTimedPermission(String permission, PermissionContext context) {
        return timedPermissions(context).contains(permission);
    }

    default boolean hasTimedPermission(String permission, String world) {
        return hasTimedPermission(permission, legacyWorldContext(world));
    }

    /** @return worlds where this subject has permissions, options, or inheritance data */
    Set<String> configuredWorlds();

    /** @return direct permissions keyed by world */
    Map<String, List<String>> permissionsByWorld();

    /** @return effective permissions keyed by world */
    Map<String, List<String>> effectivePermissionsByWorld();

    /** @return effective chat prefix in {@code context}, including inheritance */
    String prefix(PermissionContext context);

    default String prefix(String world) {
        return prefix(legacyWorldContext(world));
    }

    /** @return effective chat suffix in {@code context}, including inheritance */
    String suffix(PermissionContext context);

    default String suffix(String world) {
        return suffix(legacyWorldContext(world));
    }

    /** @return effective option value in {@code context}, including inheritance */
    String option(String key, PermissionContext context);

    default String option(String key, String world) {
        return option(key, legacyWorldContext(world));
    }

    /** @return effective options in {@code context}, including inheritance */
    Map<String, String> options(PermissionContext context);

    default Map<String, String> options(String world) {
        return options(legacyWorldContext(world));
    }

    /** @return timed permission entries across every configured world, including global */
    default List<TimedPermissionEntry> allTimedPermissionEntries() {
        java.util.LinkedHashSet<String> worlds = new java.util.LinkedHashSet<>();
        worlds.add(Worlds.GLOBAL);
        worlds.addAll(configuredWorlds());
        return worlds.stream().flatMap(world -> timedPermissionEntries(world).stream()).toList();
    }

    /** Converts legacy {@code String world} arguments to {@link PermissionContext}. */
    static PermissionContext legacyWorldContext(String world) {
        if (Worlds.isGlobal(world)) {
            return PermissionContext.global();
        }
        return PermissionContext.world(Worlds.normalize(world));
    }
}
