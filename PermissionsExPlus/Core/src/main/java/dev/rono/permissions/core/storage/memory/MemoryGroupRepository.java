package dev.rono.permissions.core.storage.memory;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.core.storage.GroupRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public final class MemoryGroupRepository implements GroupRepository {

    private final Map<String, Group> groups = new ConcurrentHashMap<>();

    @Override
    public Optional<Group> find(String name) {
        return Optional.ofNullable(groups.get(name));
    }

    @Override
    public Group save(Group group) {
        groups.put(group.name(), group);

        return group;
    }

    @Override
    public void delete(String name) {
        groups.remove(name);
    }

    @Override
    public Collection<Group> all() {
        return Collections.unmodifiableCollection(groups.values());
    }

    public void clear() {
        groups.clear();
    }
}