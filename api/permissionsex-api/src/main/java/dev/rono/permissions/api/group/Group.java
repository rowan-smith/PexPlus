package dev.rono.permissions.api.group;

import dev.rono.permissions.api.permission.PermissionHolder;

/** Modern permission group view. */
public interface Group {

    String getName();

    PermissionHolder asHolder();
}
