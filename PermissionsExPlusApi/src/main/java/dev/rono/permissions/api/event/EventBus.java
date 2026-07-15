package dev.rono.permissions.api.event;

import java.util.function.Consumer;

public interface EventBus {

    <E extends Event> Subscription subscribe(Class<E> eventType, Consumer<? super E> listener);
}
