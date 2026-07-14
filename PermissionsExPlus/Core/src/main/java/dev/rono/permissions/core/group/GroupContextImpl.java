package dev.rono.permissions.core.group;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.group.GroupContext;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.permission.PermissionResult;
import dev.rono.permissions.api.realm.Realm;
import dev.rono.permissions.core.permission.PermissionResolver;

import java.util.Collection;
import java.util.List;

public final class GroupContextImpl implements GroupContext {

    private final Group group;
    private final Realm realm;

    private final PermissionResolver resolver;

    public GroupContextImpl(Group group, Realm realm, PermissionResolver resolver) {
        this.group = group;
        this.realm = realm;
        this.resolver = resolver;
    }

    @Override
    public Realm realm() {
        return realm;
    }

    @Override
    public PermissionHolder holder() {
        return group;
    }

    @Override
    public boolean has(String permission) {
        return check(permission) == PermissionResult.TRUE;
    }

    @Override
    public PermissionResult check(String permission) {
        return resolver.resolve(group, realm, permission);
    }

    @Override
    public Collection<PermissionNode> nodes() {
        return List.of();
    }

    @Override
    public List<Group> parents() {
        return List.of();
    }

    @Override
    public void addParent(Group parent) {

    }

    @Override
    public void removeParent(Group parent) {

    }
}
