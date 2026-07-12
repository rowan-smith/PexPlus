package dev.rono.permissions.api.event;

import java.util.function.Consumer;

public interface EventBusService {

    <T extends PermissionEvent> void subscribe(
            Class<T> type,
            Consumer<T> listener
    );

    <T extends PermissionEvent> void unsubscribe(
            Class<T> type,
            Consumer<T> listener
    );

    void publish(PermissionEvent event);

}
