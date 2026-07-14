package dev.rono.permissions.api.permission;

import dev.rono.permissions.api.realm.Realm;

import java.util.Collection;

public interface PermissionContext {
    Realm realm();

    PermissionHolder holder();

    PermissionResult check(String permission);

    boolean has(String permission);

    Collection<PermissionNode> nodes();
}