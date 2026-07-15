package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.user.User;

/**
 * Read-only calculations of effective state from stored permission snapshots.
 */
public interface Resolvers {

    ResolvedData resolve(PermissionHolder holder, QueryOptions options);

    ResolvedUserData resolve(User user, QueryOptions options);

    PermissionResolver permissions();

    OptionResolver options();

    InheritanceResolver inheritance();

    PrimaryGroupResolver primaryGroup();

    DefaultGroupResolver defaultGroups();
}
