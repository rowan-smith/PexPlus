package dev.rono.permissions.api.group;

import dev.rono.permissions.api.permission.PermissionContext;

import java.util.List;

public interface GroupContext extends PermissionContext {
    List<Group> parents();

    void addParent(Group parent);

    void removeParent(Group parent);
}
