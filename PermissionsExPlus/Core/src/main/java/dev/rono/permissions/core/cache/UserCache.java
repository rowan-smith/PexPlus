package dev.rono.permissions.core.cache;

import dev.rono.permissions.api.user.User;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public final class UserCache {

    private final ConcurrentHashMap<UUID, User> users = new ConcurrentHashMap<>();

    public User get(UUID uuid) {
        return users.get(uuid);
    }

    public void put(User user) {
        users.put(user.id(), user);
    }

    public void remove(UUID uuid) {
        users.remove(uuid);
    }

    public boolean contains(UUID uuid) {
        return users.containsKey(uuid);
    }
}