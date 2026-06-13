package dev.rono.permissions.api.subject;

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

    /**
     * Adds a direct permission assignment in the given world.
     *
     * @param permission permission node to add
     * @param world      world name, or {@link Worlds#GLOBAL} for the global namespace
     */
    void addPermission(String permission, String world);

    /**
     * Removes a direct permission assignment in the given world.
     *
     * @param permission permission node to remove
     * @param world      world name, or {@link Worlds#GLOBAL} for the global namespace
     */
    void removePermission(String permission, String world);

    /**
     * Replaces direct permission assignments in the given world.
     *
     * @param permissions new permission expressions
     * @param world       world name, or {@link Worlds#GLOBAL} for the global namespace
     */
    void setPermissions(List<String> permissions, String world);

    /**
     * Adds a timed permission grant in the given world.
     *
     * @param permission       permission node to grant temporarily
     * @param world            world name, or {@link Worlds#GLOBAL} for the global namespace
     * @param lifetimeSeconds  seconds until expiry; {@code 0} for transient (in-memory only)
     */
    void addTimedPermission(String permission, String world, int lifetimeSeconds);

    /**
     * Removes a timed permission grant in the given world.
     *
     * @param permission permission node to remove from timed grants
     * @param world      world name, or {@link Worlds#GLOBAL} for the global namespace
     */
    void removeTimedPermission(String permission, String world);

    /**
     * Sets the chat prefix stored directly on this subject for the given world.
     *
     * @param prefix new prefix value
     * @param world  world name, or {@link Worlds#GLOBAL} for the global namespace
     */
    void setPrefix(String prefix, String world);

    /**
     * Sets the chat suffix stored directly on this subject for the given world.
     *
     * @param suffix new suffix value
     * @param world  world name, or {@link Worlds#GLOBAL} for the global namespace
     */
    void setSuffix(String suffix, String world);

    /**
     * Sets an option stored directly on this subject for the given world.
     *
     * @param key   option key
     * @param value option value; {@code null} or empty removes the option
     * @param world world name, or {@link Worlds#GLOBAL} for the global namespace
     */
    void setOption(String key, String value, String world);

    /**
     * Persists pending changes for this subject to the active backend.
     */
    void save();

    /**
     * Removes this subject from the backend and evicts it from in-memory caches.
     */
    void delete();
}
