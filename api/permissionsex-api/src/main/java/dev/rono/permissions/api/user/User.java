package dev.rono.permissions.api.user;

import dev.rono.permissions.api.permission.PermissionHolder;

import java.util.UUID;

/** Modern permission user view. */
public interface User {

    UUID getId();

    String getName();

    PermissionHolder asHolder();
}
