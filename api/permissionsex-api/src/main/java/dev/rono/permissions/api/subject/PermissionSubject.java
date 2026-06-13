package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.world.Worlds;
import java.util.List;
import java.util.Map;

/**
 * Shared permission-subject operations for users and groups.
 *
 * <p>Composed from internal roles {@link SubjectIdentity}, {@link PermissionView}, and
 * {@link PermissionMutator}. The public surface remains this single interface; the roles exist so
 * implementations and tests can depend on narrower contracts.</p>
 *
 * <p>{@code world} is {@link Worlds#GLOBAL} or empty for the global context (classic PEX {@code null} world).</p>
 *
 * <p>Parameterless overloads and {@link #global()} operate in the global namespace, equivalent to passing
 * {@link Worlds#GLOBAL} explicitly.</p>
 *
 * @see SubjectWorldContext world-bound projection with no duplicated engine logic
 */
public interface PermissionSubject extends SubjectIdentity, PermissionView, PermissionMutator {

    // --- Global convenience (same as passing {@link Worlds#GLOBAL}) ---

    /**
     * Checks whether this subject effectively holds the given permission in the global namespace.
     *
     * <p>Delegates to {@link #has(String, String)} with {@link Worlds#GLOBAL}. Performs full effective
     * resolution (inheritance, negation, timed grants).</p>
     *
     * @param permission permission node to check
     * @return {@code true} if the permission is granted, {@code false} otherwise
     */
    default boolean hasPermission(String permission) {
        return has(permission, Worlds.GLOBAL);
    }

    /**
     * Alias for {@link #hasPermission(String)}.
     *
     * @param permission permission node to check
     * @return {@code true} if the permission is granted, {@code false} otherwise
     */
    default boolean has(String permission) {
        return hasPermission(permission);
    }

    /**
     * Returns direct permission assignments in the global namespace.
     *
     * <p>Delegates to {@link #permissions(String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @return unmodifiable list of own permission expressions (not inherited)
     */
    default List<String> permissions() {
        return permissions(Worlds.GLOBAL);
    }

    /**
     * Returns effective permissions in the global namespace after inheritance.
     *
     * <p>Delegates to {@link #effectivePermissions(String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @return unmodifiable list of effective permission expressions
     */
    default List<String> effectivePermissions() {
        return effectivePermissions(Worlds.GLOBAL);
    }

    /**
     * Adds a permission assignment in the global namespace.
     *
     * <p>Delegates to {@link #addPermission(String, String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @param permission permission node to add
     */
    default void addPermission(String permission) {
        addPermission(permission, Worlds.GLOBAL);
    }

    /**
     * Removes a permission assignment in the global namespace.
     *
     * <p>Delegates to {@link #removePermission(String, String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @param permission permission node to remove
     */
    default void removePermission(String permission) {
        removePermission(permission, Worlds.GLOBAL);
    }

    /**
     * Replaces direct permission assignments in the global namespace.
     *
     * <p>Delegates to {@link #setPermissions(List, String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @param permissions new permission expressions
     */
    default void setPermissions(List<String> permissions) {
        setPermissions(permissions, Worlds.GLOBAL);
    }

    /**
     * Adds a timed permission grant in the global namespace.
     *
     * <p>Delegates to {@link #addTimedPermission(String, String, int)} with {@link Worlds#GLOBAL}.</p>
     *
     * @param permission       permission node to grant temporarily
     * @param lifetimeSeconds  seconds until expiry; {@code 0} for transient (in-memory only)
     */
    default void addTimedPermission(String permission, int lifetimeSeconds) {
        addTimedPermission(permission, Worlds.GLOBAL, lifetimeSeconds);
    }

    /**
     * Removes a timed permission grant in the global namespace.
     *
     * <p>Delegates to {@link #removeTimedPermission(String, String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @param permission permission node to remove from timed grants
     */
    default void removeTimedPermission(String permission) {
        removeTimedPermission(permission, Worlds.GLOBAL);
    }

    /**
     * Returns permission nodes with active timed grants in the global namespace.
     *
     * <p>Delegates to {@link #timedPermissions(String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @return list of timed permission nodes
     */
    default List<String> timedPermissions() {
        return timedPermissions(Worlds.GLOBAL);
    }

    /**
     * Returns timed permission entries with remaining lifetime metadata in the global namespace.
     *
     * <p>Delegates to {@link #timedPermissionEntries(String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @return list of timed permission entries
     */
    default List<TimedPermissionEntry> timedPermissionEntries() {
        return timedPermissionEntries(Worlds.GLOBAL);
    }

    /**
     * Returns seconds remaining on a timed permission grant in the global namespace.
     *
     * <p>Delegates to {@link #timedPermissionRemainingSeconds(String, String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @param permission timed permission node
     * @return seconds until expiry; {@code 0} if the timed permission is absent
     */
    default int timedPermissionRemainingSeconds(String permission) {
        return timedPermissionRemainingSeconds(permission, Worlds.GLOBAL);
    }

    /**
     * Returns whether the permission is directly assigned as a timed grant in the global namespace.
     *
     * <p>Delegates to {@link #hasTimedPermission(String, String)} with {@link Worlds#GLOBAL}. Unlike
     * {@link #has(String)}, this does not perform full effective permission resolution.</p>
     *
     * @param permission permission node to look up
     * @return {@code true} if a timed grant exists for the node, {@code false} otherwise
     */
    default boolean hasTimedPermission(String permission) {
        return hasTimedPermission(permission, Worlds.GLOBAL);
    }

    /**
     * Returns the effective chat prefix in the global namespace.
     *
     * <p>Delegates to {@link #prefix(String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @return resolved prefix, or empty string when none is defined
     */
    default String prefix() {
        return prefix(Worlds.GLOBAL);
    }

    /**
     * Returns the effective chat suffix in the global namespace.
     *
     * <p>Delegates to {@link #suffix(String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @return resolved suffix, or empty string when none is defined
     */
    default String suffix() {
        return suffix(Worlds.GLOBAL);
    }

    /**
     * Sets the chat prefix stored directly on this subject in the global namespace.
     *
     * <p>Delegates to {@link #setPrefix(String, String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @param prefix new prefix value
     */
    default void setPrefix(String prefix) {
        setPrefix(prefix, Worlds.GLOBAL);
    }

    /**
     * Sets the chat suffix stored directly on this subject in the global namespace.
     *
     * <p>Delegates to {@link #setSuffix(String, String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @param suffix new suffix value
     */
    default void setSuffix(String suffix) {
        setSuffix(suffix, Worlds.GLOBAL);
    }

    /**
     * Returns an effective option value in the global namespace.
     *
     * <p>Delegates to {@link #option(String, String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @param key option key
     * @return option value, or {@code null} if unset
     */
    default String option(String key) {
        return option(key, Worlds.GLOBAL);
    }

    /**
     * Sets an option stored directly on this subject in the global namespace.
     *
     * <p>Delegates to {@link #setOption(String, String, String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @param key   option key
     * @param value option value; {@code null} or empty removes the option
     */
    default void setOption(String key, String value) {
        setOption(key, value, Worlds.GLOBAL);
    }

    /**
     * Returns effective options in the global namespace.
     *
     * <p>Delegates to {@link #options(String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @return map of option keys to resolved values
     */
    default Map<String, String> options() {
        return options(Worlds.GLOBAL);
    }

    /**
     * Returns a world-scoped view of this subject for permission and metadata operations.
     *
     * <p>Methods on the returned context apply to {@code world} without repeating the world argument.
     * {@link Worlds#GLOBAL} selects the global namespace. The context is a thin facade — it must not
     * contain resolution or persistence logic.</p>
     *
     * @param world world name, or {@link Worlds#GLOBAL} for the global namespace
     * @return world-bound context for this subject
     */
    default SubjectWorldContext inWorld(String world) {
        return SubjectWorldContexts.subject(this, world);
    }

    /**
     * Returns a view of this subject bound to the global namespace.
     *
     * <p>Equivalent to {@link #inWorld(String)} with {@link Worlds#GLOBAL}.</p>
     *
     * @return global world context for this subject
     */
    default SubjectWorldContext global() {
        return inWorld(Worlds.GLOBAL);
    }
}
