package dev.rono.permissions.api;

import dev.rono.permissions.api.event.EventBus;
import dev.rono.permissions.api.group.GroupManager;
import dev.rono.permissions.api.ladder.LadderManager;
import dev.rono.permissions.api.command.CommandManager;
import dev.rono.permissions.api.permission.PermissionManager;
import dev.rono.permissions.api.realm.RealmManager;
import dev.rono.permissions.api.user.UserManager;

public interface PermissionsExPlusApi {
    UserManager users();

    GroupManager groups();

    LadderManager ladders();

    RealmManager realms();

    PermissionManager permissions();

    CommandManager commands();

    EventBus events();
}