package dev.rono.permissions.api.event;

import java.util.function.Consumer;

public interface SimpleEventBus extends EventManager {

    <T extends PermissionEvent> void subscribe(
            Class<T> type,
            Consumer<T> listener
    );

    void publish(PermissionEvent event);

    <T extends PermissionEvent> void unsubscribe(
            Class<T> type,
            Consumer<T> listener
    );

}
