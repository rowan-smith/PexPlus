package dev.rono.permissions.api.group;

import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.subject.PermissionSubject;

import java.util.Collection;

public interface Group extends PermissionSubject {
    Collection<PermissionNode> permissions();

    Collection<String> parents();

    void addParent(Group group);

    void removeParent(Group group);
}
