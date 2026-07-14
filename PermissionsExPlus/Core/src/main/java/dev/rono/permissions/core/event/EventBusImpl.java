package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.EventBus;
import dev.rono.permissions.api.event.EventSubscription;
import dev.rono.permissions.api.event.PermissionEvent;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;


public final class EventBusImpl implements EventBus {

    private final ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<EventSubscription<?>>> listeners = new ConcurrentHashMap<>();

    @Override
    public <T extends PermissionEvent> EventSubscription<T> subscribe(Class<T> type, Consumer<T> listener) {
        var subscription = new SimpleEventSubscription<>(type, listener);

        listeners.computeIfAbsent(type, x -> new CopyOnWriteArrayList<>()).add(subscription);

        return subscription;
    }

    @Override
    public void publish(PermissionEvent event) {
        List<EventSubscription<?>> subscriptions = listeners.get(event.getClass());

        if (subscriptions == null) {
            return;
        }

        subscriptions.forEach(sub -> dispatch(sub, event));
    }

    @SuppressWarnings("unchecked")
    private static <T extends PermissionEvent> void dispatch(EventSubscription<T> sub, PermissionEvent event) {
        sub.fire((T) event);
    }
}