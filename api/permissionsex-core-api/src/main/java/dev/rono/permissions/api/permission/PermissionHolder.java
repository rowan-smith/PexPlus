package dev.rono.permissions.api.permission;

import java.util.UUID;

/** Permission target identity for add/remove/has operations. */
public interface PermissionHolder {

    UUID getId();

    HolderType getType();
}
