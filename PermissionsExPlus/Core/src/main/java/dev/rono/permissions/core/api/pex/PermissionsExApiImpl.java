package dev.rono.permissions.core.api.pex;

import dev.rono.permissions.api.PermissionsExApi;
import dev.rono.permissions.api.PermissionsExPlusApi;
import dev.rono.permissions.api.event.EventManager;
import dev.rono.permissions.api.event.PermissionEventBus;
import dev.rono.permissions.api.group.GroupManager;
import dev.rono.permissions.api.ladder.LadderManager;
import dev.rono.permissions.api.permission.PermissionContext;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionManager;
import dev.rono.permissions.api.permission.PermissionResult;
import dev.rono.permissions.api.realm.RealmManager;
import dev.rono.permissions.api.user.UserManager;
import dev.rono.permissions.core.DefaultPermissionManager;

public final class PermissionsExApiImpl implements PermissionsExApi, PermissionsExPlusApi {

    private final DefaultPermissionManager manager;
    private final UserManager userManager;
    private final GroupManager groupManager;
    private final DefaultRealmManager realmManager;
    private final LadderManager ladderManager;

    public PermissionsExApiImpl(DefaultPermissionManager manager) {
        this.manager = manager;
        this.userManager = new DefaultUserManager(manager);
        this.groupManager = new DefaultGroupManager(manager);
        this.realmManager = new DefaultRealmManager(manager);
        this.ladderManager = new DefaultLadderManager(manager);
    }

    @Override
    public UserManager getUserManager() {
        return userManager;
    }

    @Override
    public GroupManager getGroupManager() {
        return groupManager;
    }

    @Override
    public RealmManager getRealmManager() {
        return realmManager;
    }

    @Override
    public LadderManager getLadderManager() {
        return ladderManager;
    }

    @Override
    public ru.tehkode.permissions.PermissionManager getPermissionManager() {
        return manager;
    }

    @Override
    public PermissionEventBus getEventBus() {
        return manager.events();
    }

    @Override
    public UserManager users() {
        return userManager;
    }

    @Override
    public GroupManager groups() {
        return groupManager;
    }

    @Override
    public RealmManager realms() {
        return realmManager;
    }

    @Override
    public LadderManager ladders() {
        return ladderManager;
    }

    @Override
    public EventManager events() {
        return manager.events();
    }

    @Override
    public PermissionManager permissions() {
        return new PermissionManager() {
            @Override
            public PermissionResult check(PermissionHolder holder, String permission, PermissionContext context) {
                return manager.hasPermission(holder, permission, context)
                        ? PermissionResult.allow()
                        : PermissionResult.deny();
            }

            @Override
            public void add(PermissionHolder holder, String permission, PermissionContext context) {
                manager.addPermission(holder, permission);
            }

            @Override
            public void remove(PermissionHolder holder, String permission, PermissionContext context) {
                manager.removePermission(holder, permission);
            }
        };
    }
}
