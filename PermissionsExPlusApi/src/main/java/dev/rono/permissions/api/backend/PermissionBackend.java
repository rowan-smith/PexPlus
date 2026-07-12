package dev.rono.permissions.api.backend;

import dev.rono.permissions.api.data.GroupData;
import dev.rono.permissions.api.data.UserData;

import java.util.UUID;

public interface PermissionBackend {

    UserData loadUser(UUID uuid);

    void saveUser(UserData data);

    void deleteUser(UUID uuid);

    GroupData loadGroup(String name);

    void saveGroup(GroupData data);

    void deleteGroup(String name);

}
