package dev.rono.permissions.api.user;

import java.util.Optional;
import java.util.UUID;

/**
 * User registry with an explicit lifecycle: {@code find} never materializes, {@code get} requires
 * persistence, {@code create} explicitly creates a new record.
 */
public interface UserManager {

    Optional<User> findUser(UUID uuid);

    Optional<User> findUser(String name);

    User getUser(UUID uuid) throws UserNotFoundException;

    User getUser(String name) throws UserNotFoundException;

    User createUser(UUID uuid) throws UserAlreadyExistsException;

    User createUser(String name) throws UserAlreadyExistsException;

    boolean exists(UUID uuid);

    boolean exists(String name);
}
