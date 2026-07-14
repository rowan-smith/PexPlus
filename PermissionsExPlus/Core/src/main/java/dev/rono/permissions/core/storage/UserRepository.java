package dev.rono.permissions.core.storage;

import dev.rono.permissions.api.user.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository {

    Optional<User> find(UUID uuid);

    User save(User user);

    void delete(UUID uuid);

    Collection<User> all();
}