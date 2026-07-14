package dev.rono.permissions.api.permission;

public interface PermissionManager {
    PermissionResult check(PermissionHolder holder, String permission, PermissionContext context);
}