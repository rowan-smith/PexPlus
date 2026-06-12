package dev.rono.permissions.core.api;

import dev.rono.permissions.api.bus.PexEntityDispatch;
import dev.rono.permissions.api.bus.PexPermissionDispatch;
import dev.rono.permissions.api.bus.PexSystemDispatch;
import dev.rono.permissions.api.event.PexPermissionEventBus;
import dev.rono.permissions.api.event.PexPermissionEventListener;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public final class DefaultPermissionEventBus implements PexPermissionEventBus {
    private final AtomicLong nextId = new AtomicLong();
    private final CopyOnWriteArrayList<Entry> listeners = new CopyOnWriteArrayList<>();

    public void dispatch(PexPermissionDispatch dispatch) {
        for (Entry entry : listeners) {
            if (dispatch instanceof PexEntityDispatch entity) {
                entry.listener.onEntity(entity);
            } else if (dispatch instanceof PexSystemDispatch system) {
                entry.listener.onSystem(system);
            }
        }
    }

    @Override
    public Subscription subscribe(PexPermissionEventListener listener) {
        Objects.requireNonNull(listener, "listener");
        Entry entry = new Entry(nextId.incrementAndGet(), listener);
        listeners.add(entry);
        return entry;
    }

    @Override
    public void unsubscribe(Subscription subscription) {
        if (subscription instanceof Entry entry) {
            listeners.remove(entry);
        }
    }

    private record Entry(long id, PexPermissionEventListener listener) implements Subscription {}
}
