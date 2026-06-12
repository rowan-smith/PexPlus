package dev.rono.permissions.api.permission;

import dev.rono.permissions.api.user.User;

import java.time.Duration;
import java.util.List;

/**
 * Holder-based permission operations (PEX-style add/remove/has with builder support).
 */
public interface PermissionService {

    PermissionNode addPermission(PermissionHolder holder, String permission);

    PermissionNode addPermission(PermissionHolder holder, String permission, Duration duration);

    PermissionNode addPermission(PermissionAddRequest request);

    void removePermission(PermissionHolder holder, String permission);

    boolean hasPermission(PermissionHolder holder, String permission);

    List<PermissionNode> getPermissions(PermissionHolder holder);

    default PermissionNode addPermission(User user, String permission) {
        return addPermission(user.asHolder(), permission);
    }

    default PermissionNode addPermission(User user, String permission, Duration duration) {
        return addPermission(user.asHolder(), permission, duration);
    }
}
