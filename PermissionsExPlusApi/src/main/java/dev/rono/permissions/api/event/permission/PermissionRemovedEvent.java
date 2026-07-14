package dev.rono.permissions.api.event.permission;

import dev.rono.permissions.api.event.PermissionEvent;
import dev.rono.permissions.api.permission.PermissionNode;

public interface PermissionRemovedEvent extends PermissionEvent {
    PermissionNode node();
}
