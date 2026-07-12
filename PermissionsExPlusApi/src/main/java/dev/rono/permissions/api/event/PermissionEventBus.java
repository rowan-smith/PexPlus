package dev.rono.permissions.api.event;

public interface PermissionEventBus extends EventManager {

    Subscription subscribe(PermissionEventListener listener);

    void unsubscribe(Subscription subscription);

    interface Subscription {}
}
