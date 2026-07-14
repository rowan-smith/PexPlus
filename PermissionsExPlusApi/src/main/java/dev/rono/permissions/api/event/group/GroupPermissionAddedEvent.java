package dev.rono.permissions.api.event.group;

import dev.rono.permissions.api.permission.PermissionNode;

public interface GroupPermissionAddedEvent extends GroupEvent {
    PermissionNode node();
}
