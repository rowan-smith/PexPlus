package dev.rono.permissions.api.world;

import dev.rono.permissions.api.permission.PermissionHolder;

/** Registered server realm (world) for permission scoping. */
public interface World {

    String getName();

    PermissionHolder asHolder();
}
