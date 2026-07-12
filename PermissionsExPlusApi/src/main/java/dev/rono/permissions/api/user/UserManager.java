package dev.rono.permissions.api.user;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface UserManager {
    /**
     * Finds a user currently loaded in memory.
     *
     * @param uuid user UUID
     * @return cached user if currently loaded
     */
    Optional<PermissionUser> find(UUID uuid);

    /**
     * Loads a user from the persistent storage.
     *
     * @param uuid user UUID
     * @return loaded user instance
     * @throws UserNotFoundException if the user does not exist
     */
    PermissionUser load(UUID uuid);

    /**
     * Unloads a user from memory.
     *
     * @param uuid user UUID
     */
    void unload(UUID uuid);

    /**
     * Unloads all users from memory.
     */
    void unloadAll();

    /**
     * Reports whether a user is currently loaded in memory.
     *
     * @param uuid user UUID
     * @return {@code true} if the user is currently cached
     */
    boolean isLoaded(UUID uuid);

    /**
     * Creates a new persistent user record.
     *
     * @param uuid user UUID
     * @return created user
     * @throws UserAlreadyExistsException if user already exists
     */
    PermissionUser create(UUID uuid);

    /**
     * Reports whether a user record exists for the UUID.
     *
     * @param uuid user UUID
     * @return {@code true} if persisted or already materialized in memory
     */
    boolean exists(UUID uuid);

    /**
     * Returns the number of user records stored in the active backend.
     *
     * @return total persisted user count
     */
    int count();

    /**
     * Returns how many persisted users match {@code filter}.
     *
     * @param filter predicate applied to each stored user; must not be {@code null}
     * @return count of users for which the predicate is {@code true}
     */
    int count(Predicate<PermissionUser> filter);
}
