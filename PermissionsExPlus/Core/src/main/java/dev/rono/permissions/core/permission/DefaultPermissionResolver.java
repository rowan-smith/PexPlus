package dev.rono.permissions.core.permission;

import dev.rono.permissions.api.group.GroupManager;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionResult;
import dev.rono.permissions.api.realm.Realm;


public final class DefaultPermissionResolver implements PermissionResolver {

    private final GroupManager groups;

    public DefaultPermissionResolver(GroupManager groups) {
        this.groups = groups;
    }

    @Override
    public PermissionResult resolve(PermissionHolder holder, Realm realm, String node) {
        // TODO:
        // 1 holder permissions
        // 2 inherited groups
        // 3 wildcard

        return PermissionResult.UNDEFINED;
    }

}