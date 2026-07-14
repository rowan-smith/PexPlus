package dev.rono.permissions.api.event;

public interface EventSubscription<T extends PermissionEvent> {
    void fire(T event);

    void cancel();

    boolean active();
}