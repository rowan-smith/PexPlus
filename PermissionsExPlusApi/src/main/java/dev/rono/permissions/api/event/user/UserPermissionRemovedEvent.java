package dev.rono.permissions.api.event.user;

import dev.rono.permissions.api.permission.PermissionNode;

public interface UserPermissionRemovedEvent extends UserEvent {
    PermissionNode node();
}