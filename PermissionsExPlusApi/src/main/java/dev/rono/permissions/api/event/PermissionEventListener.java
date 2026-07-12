package dev.rono.permissions.api.event;

import dev.rono.permissions.api.bus.EntityDispatch;
import dev.rono.permissions.api.bus.SystemDispatch;

public interface PermissionEventListener {

    default void onEntity(EntityDispatch dispatch) {}

    default void onSystem(SystemDispatch dispatch) {}
}
