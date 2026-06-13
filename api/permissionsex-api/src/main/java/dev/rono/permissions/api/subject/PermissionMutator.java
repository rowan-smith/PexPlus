package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.permission.PermissionContext;
import dev.rono.permissions.api.world.Worlds;
import java.util.List;

/**
 * Write operations for a permission subject.
 *
 * <p>Internal role interface — composed by {@link PermissionSubject}. Subjects obtained from
 * {@link dev.rono.permissions.api.user.UserManager} / {@link dev.rono.permissions.api.group.GroupManager}
 * are the supported mutation surface. Implementations must delegate to the engine layer without
 * duplicating business rules.</p>
 */
public interface PermissionMutator {

    /** Adds a direct permission assignment in {@code context}. */
    void addPermission(String permission, PermissionContext context);

    default void addPermission(String permission, String world) {
        addPermission(permission, PermissionView.legacyWorldContext(world));
    }

    /** Removes a direct permission assignment in {@code context}. */
    void removePermission(String permission, PermissionContext context);

    default void removePermission(String permission, String world) {
        removePermission(permission, PermissionView.legacyWorldContext(world));
    }

    /** Replaces direct permission assignments in {@code context}. */
    void setPermissions(List<String> permissions, PermissionContext context);

    default void setPermissions(List<String> permissions, String world) {
        setPermissions(permissions, PermissionView.legacyWorldContext(world));
    }

    /** Adds a timed permission grant in {@code context}. */
    void addTimedPermission(String permission, PermissionContext context, int lifetimeSeconds);

    default void addTimedPermission(String permission, String world, int lifetimeSeconds) {
        addTimedPermission(permission, PermissionView.legacyWorldContext(world), lifetimeSeconds);
    }

    /** Removes a timed permission grant in {@code context}. */
    void removeTimedPermission(String permission, PermissionContext context);

    default void removeTimedPermission(String permission, String world) {
        removeTimedPermission(permission, PermissionView.legacyWorldContext(world));
    }

    /** Sets the chat prefix stored directly on this subject in {@code context}. */
    void setPrefix(String prefix, PermissionContext context);

    default void setPrefix(String prefix, String world) {
        setPrefix(prefix, PermissionView.legacyWorldContext(world));
    }

    /** Sets the chat suffix stored directly on this subject in {@code context}. */
    void setSuffix(String suffix, PermissionContext context);

    default void setSuffix(String suffix, String world) {
        setSuffix(suffix, PermissionView.legacyWorldContext(world));
    }

    /** Sets an option stored directly on this subject in {@code context}. */
    void setOption(String key, String value, PermissionContext context);

    default void setOption(String key, String value, String world) {
        setOption(key, value, PermissionView.legacyWorldContext(world));
    }

    /** Persists pending changes for this subject to the active backend. */
    void save();

    /** Removes this subject from the backend and evicts it from in-memory caches. */
    void delete();
}
