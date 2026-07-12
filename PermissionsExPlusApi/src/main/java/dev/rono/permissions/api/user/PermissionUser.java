package dev.rono.permissions.api.user;

import dev.rono.permissions.api.permission.PermissionHolder;

import java.util.UUID;

public interface PermissionUser {

    UUID id();

    String username();

    PermissionHolder holder();

}
