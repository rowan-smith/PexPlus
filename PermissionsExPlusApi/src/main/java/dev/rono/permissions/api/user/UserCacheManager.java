package dev.rono.permissions.api.user;

import dev.rono.permissions.api.managers.CacheManager;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserCacheManager extends CacheManager<UUID, User> {

    Optional<User> get(String username);

    Set<String> names();
}
