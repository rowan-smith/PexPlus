package dev.rono.permissions.api.group;

import dev.rono.permissions.api.permission.PermissionNode;

import java.util.Collection;
import java.util.Optional;

public interface GroupManager {

    Optional<Group> find(String name);

    Group load(String name);

    Group create(String name);

    void delete(String name);

    void addPermission(Group group, String permission);

    void removePermission(Group group, String permission);

    void addParent(Group group, Group parent);

    void removeParent(Group group, Group parent);

    Collection<Group> all();
}
