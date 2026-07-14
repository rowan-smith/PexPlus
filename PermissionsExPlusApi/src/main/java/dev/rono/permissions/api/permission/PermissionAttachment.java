package dev.rono.permissions.api.permission;

public interface PermissionAttachment {
    PermissionNode node();

    PermissionHolder holder();

    void remove();
}
