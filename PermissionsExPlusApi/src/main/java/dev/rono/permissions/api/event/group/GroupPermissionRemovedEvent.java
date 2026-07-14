package dev.rono.permissions.api.event.group;

import dev.rono.permissions.api.permission.PermissionNode;

public interface GroupPermissionRemovedEvent extends GroupEvent {
    PermissionNode node();
}
