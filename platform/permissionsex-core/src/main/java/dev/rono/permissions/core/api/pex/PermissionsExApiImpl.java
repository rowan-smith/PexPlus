package dev.rono.permissions.core.api.pex;

import dev.rono.permissions.api.group.GroupManager;
import dev.rono.permissions.api.ladder.LadderManager;
import dev.rono.permissions.api.permission.PermissionService;
import dev.rono.permissions.api.user.UserManager;
import dev.rono.permissions.api.world.WorldManager;
import dev.rono.permissions.core.DefaultPermissionManager;

/** Modern API manager wiring for {@link DefaultPermissionManager}. */
public final class PermissionsExApiImpl {

    private final UserManager userManager;
    private final GroupManager groupManager;
    private final WorldManager worldManager;
    private final LadderManager ladderManager;
    private final PermissionService permissionService;

    public PermissionsExApiImpl(DefaultPermissionManager manager) {
        this.userManager = new DefaultUserManager(manager);
        this.groupManager = new DefaultGroupManager(manager);
        this.worldManager = new DefaultWorldManager(manager);
        this.ladderManager = new DefaultLadderManager(manager);
        this.permissionService = new HolderPermissionService(manager);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public LadderManager getLadderManager() {
        return ladderManager;
    }

    public PermissionService getPermissionService() {
        return permissionService;
    }
}
