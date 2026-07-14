package dev.rono.permissions.api.user;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserManager {

    Optional<User> find(UUID uuid);

    User load(UUID uuid);

    User create(UUID uuid);

    void unload(UUID uuid);

    boolean loaded(UUID uuid);

    void addPermission(User user, String permission);

    void removePermission(User user, String permission);

    Collection<User> all();
}
