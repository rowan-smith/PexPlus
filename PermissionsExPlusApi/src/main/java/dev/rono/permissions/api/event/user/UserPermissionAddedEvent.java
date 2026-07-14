package dev.rono.permissions.api.event.user;

import dev.rono.permissions.api.permission.PermissionNode;

public interface UserPermissionAddedEvent extends UserEvent {
    PermissionNode node();
}