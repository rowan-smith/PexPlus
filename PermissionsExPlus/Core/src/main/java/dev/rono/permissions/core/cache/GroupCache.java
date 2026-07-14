package dev.rono.permissions.core.cache;

import dev.rono.permissions.api.group.Group;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GroupCache {

    private final Map<String, Group> groups = new ConcurrentHashMap<>();

    public Group get(String name) {
        return groups.get(name);
    }

    public void put(Group group) {
        groups.put(group.name(), group);
    }

    public void remove(String name) {
        groups.remove(name);
    }

    public boolean contains(String name) {
        return groups.containsKey(name);
    }
}
