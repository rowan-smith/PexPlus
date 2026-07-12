package dev.rono.permissions.api.group;

import dev.rono.permissions.api.permission.PermissionHolder;

public interface PermissionGroup {

    String name();

    PermissionHolder holder();

}
