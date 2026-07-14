package dev.rono.permissions.api.permission;

import dev.rono.permissions.api.realm.Realm;


public interface PermissionHolder {
    PermissionContext context(Realm realm);

    void addPermission(PermissionNode permission);

    void removePermission(String permission, Realm realm);
}
