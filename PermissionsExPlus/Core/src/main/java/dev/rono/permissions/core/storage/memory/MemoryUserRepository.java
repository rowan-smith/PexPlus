package dev.rono.permissions.core.storage.memory;

import dev.rono.permissions.api.user.User;
import dev.rono.permissions.core.storage.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public final class MemoryUserRepository implements UserRepository {

    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    @Override
    public Optional<User> find(UUID uuid) {
        return Optional.ofNullable(users.get(uuid));
    }

    @Override
    public User save(User user) {
        users.put(user.id(), user);

        return user;
    }

    @Override
    public void delete(UUID uuid) {
        users.remove(uuid);
    }

    @Override
    public Collection<User> all() {
        return Collections.unmodifiableCollection(users.values());
    }

    public void clear() {
        users.clear();
    }
}