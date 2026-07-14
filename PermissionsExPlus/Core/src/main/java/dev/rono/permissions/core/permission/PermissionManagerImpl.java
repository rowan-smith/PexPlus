package dev.rono.permissions.core.permission;

import dev.rono.permissions.api.permission.PermissionContext;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionManager;
import dev.rono.permissions.api.permission.PermissionResult;


public final class PermissionManagerImpl implements PermissionManager {
    private final PermissionResolver resolver;

    public PermissionManagerImpl(PermissionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public PermissionResult check(PermissionHolder holder, String permission, PermissionContext context) {
        return resolver.resolve(holder, context.realm(), permission);
    }
}
