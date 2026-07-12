package dev.rono.permissions.api.permission;

public interface PermissionManager {

    PermissionResult check(
            PermissionHolder holder,
            String permission,
            PermissionContext context
    );

    default PermissionResult check(
            PermissionHolder holder,
            String permission
    ) {
        return check(holder, permission, PermissionContext.global());
    }

    void add(
            PermissionHolder holder,
            String permission,
            PermissionContext context
    );

    default void add(
            PermissionHolder holder,
            String permission
    ) {
        add(holder, permission, PermissionContext.global());
    }

    void remove(
            PermissionHolder holder,
            String permission,
            PermissionContext context
    );

    default void remove(
            PermissionHolder holder,
            String permission
    ) {
        remove(holder, permission, PermissionContext.global());
    }

}
