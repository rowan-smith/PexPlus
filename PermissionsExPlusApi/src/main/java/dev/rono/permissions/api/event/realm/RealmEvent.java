package dev.rono.permissions.api.event.realm;

import dev.rono.permissions.api.event.PermissionEvent;
import dev.rono.permissions.api.realm.Realm;

public interface RealmEvent extends PermissionEvent {
    Realm realm();
}
