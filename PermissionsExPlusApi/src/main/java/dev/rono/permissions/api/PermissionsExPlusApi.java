package dev.rono.permissions.api;

import dev.rono.permissions.api.event.EventManager;
import dev.rono.permissions.api.group.GroupManager;
import dev.rono.permissions.api.ladder.LadderManager;
import dev.rono.permissions.api.permission.PermissionManager;
import dev.rono.permissions.api.realm.RealmManager;
import dev.rono.permissions.api.user.UserManager;

public interface PermissionsExPlusApi {

    UserManager users();

    GroupManager groups();

    RealmManager realms();

    LadderManager ladders();

    EventManager events();

    PermissionManager permissions();
}
