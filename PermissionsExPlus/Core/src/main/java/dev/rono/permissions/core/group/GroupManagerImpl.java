package dev.rono.permissions.core.group;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.group.GroupAlreadyExistsException;
import dev.rono.permissions.api.group.GroupManager;
import dev.rono.permissions.api.group.GroupNotFoundException;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.realm.BuiltinRealm;
import dev.rono.permissions.core.cache.GroupCache;
import dev.rono.permissions.core.permission.PermissionNodeImpl;
import dev.rono.permissions.core.permission.PermissionResolver;
import dev.rono.permissions.core.storage.GroupRepository;

import java.util.Collection;
import java.util.Optional;

public final class GroupManagerImpl implements GroupManager {

    private final GroupCache cache;
    private final GroupRepository repository;
    private final PermissionResolver resolver;

    public GroupManagerImpl(GroupCache cache, GroupRepository repository, PermissionResolver resolver) {
        this.cache = cache;
        this.repository = repository;
        this.resolver = resolver;
    }

    @Override
    public Optional<Group> find(String name) {
        return Optional.ofNullable(cache.get(name));
    }

    @Override
    public Group load(String name) {
        Group group = repository.find(name).orElseThrow(() -> new GroupNotFoundException(name));
        cache.put(group);
        return group;
    }

    @Override
    public Group create(String name) {
        if (cache.contains(name)) {
            throw new GroupAlreadyExistsException(name);
        }
        Group group = new GroupImpl(name, resolver);
        repository.save(group);
        cache.put(group);
        return group;
    }

    @Override
    public void delete(String name) {
        repository.delete(name);
        cache.remove(name);
    }

    @Override
    public void addPermission(Group group, String permission) {
        group.addPermission(new PermissionNodeImpl(permission));
    }

    @Override
    public void removePermission(Group group, String permission) {
        group.removePermission(permission, BuiltinRealm.GLOBAL);
    }

    @Override
    public void addParent(Group group, Group parent) {
        group.addParent(parent);
    }

    @Override
    public void removeParent(Group group, Group parent) {
        group.removeParent(parent);
    }

    @Override
    public Collection<Group> all() {
        return repository.all();
    }
}
