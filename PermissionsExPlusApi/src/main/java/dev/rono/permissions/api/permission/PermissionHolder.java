package dev.rono.permissions.api.permission;

import java.util.UUID;

public interface PermissionHolder {

    UUID getId();

    HolderType getType();

}
