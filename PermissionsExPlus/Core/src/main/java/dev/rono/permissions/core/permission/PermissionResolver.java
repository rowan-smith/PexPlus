package dev.rono.permissions.core.permission;

import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionResult;
import dev.rono.permissions.api.realm.Realm;

public interface PermissionResolver {
    PermissionResult resolve(PermissionHolder holder, Realm realm, String permission);
}
