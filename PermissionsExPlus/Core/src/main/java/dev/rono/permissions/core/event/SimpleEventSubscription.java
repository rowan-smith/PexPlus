package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.EventSubscription;
import dev.rono.permissions.api.event.PermissionEvent;

import java.util.function.Consumer;

public final class SimpleEventSubscription<T extends PermissionEvent> implements EventSubscription<T> {

    private final Class<T> type;
    private final Consumer<T> consumer;

    private boolean active = true;

    public SimpleEventSubscription(Class<T> type, Consumer<T> consumer) {
        this.type = type;
        this.consumer = consumer;
    }

    @Override
    public void fire(T event) {
        if (!active) {
            return;
        }

        consumer.accept(type.cast(event));
    }

    @Override
    public void cancel() {
        active = false;
    }

    @Override
    public boolean active() {
        return active;
    }
}