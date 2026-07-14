package dev.rono.permissions.core.group;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.permission.PermissionContext;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.realm.Realm;
import dev.rono.permissions.api.subject.SubjectType;
import dev.rono.permissions.core.permission.PermissionResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GroupImpl implements Group, PermissionHolder {

    private final String name;
    private final PermissionResolver resolver;
    private final List<PermissionNode> permissions = new ArrayList<>();
    private final List<String> parents = new ArrayList<>();

    public GroupImpl(String name, PermissionResolver resolver) {
        this.name = name;
        this.resolver = resolver;
    }

    @Override
    public Collection<PermissionNode> permissions() {
        return List.copyOf(permissions);
    }

    @Override
    public Collection<String> parents() {
        return List.copyOf(parents);
    }

    @Override
    public void addParent(Group group) {
        parents.add(group.name());
    }

    @Override
    public void removeParent(Group group) {
        parents.remove(group.name());
    }

    @Override
    public PermissionContext context(Realm realm) {
        return new GroupContextImpl(this, realm, resolver);
    }

    @Override
    public void addPermission(PermissionNode permission) {
        permissions.add(permission);
    }

    @Override
    public void removePermission(String permission, Realm realm) {
        permissions.removeIf(n -> n.permission().equals(permission) && n.realm().equals(realm));
    }

    @Override
    public SubjectType type() {
        return SubjectType.GROUP;
    }

    @Override
    public String name() {
        return name;
    }
}
