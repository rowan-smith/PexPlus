package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.permission.PermissionResult;

import java.util.Map;

/** Immutable effective permissions, suitable for repeated checks. */
public interface ResolvedPermissionData {

    PermissionResult check(String permission);

    /**
     * Returns applicable stored permission expressions after context, expiry,
     * inheritance, and default filtering. Wildcard expressions remain unexpanded.
     */
    Map<String, PermissionResult> permissionMap();
}
