package dev.rono.permissions.api.event;

import java.util.function.Consumer;

public interface EventBus {
    <T extends PermissionEvent> EventSubscription<T> subscribe(Class<T> type, Consumer<T> listener);

    void publish(PermissionEvent event);
}