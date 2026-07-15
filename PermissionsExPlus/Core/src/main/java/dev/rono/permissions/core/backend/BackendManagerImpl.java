package dev.rono.permissions.core.backend;

import dev.rono.permissions.api.backend.Backend;
import dev.rono.permissions.api.backend.BackendManager;
import dev.rono.permissions.api.backend.BackendStatus;
import dev.rono.permissions.core.config.BackendType;
import dev.rono.permissions.core.store.DataStore;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class BackendManagerImpl implements BackendManager {
    private final DataStore store;
    private final BooleanSupplier running;

    public BackendManagerImpl(DataStore store, BooleanSupplier running) {
        this.store = Objects.requireNonNull(store, "store");
        this.running = Objects.requireNonNull(running, "running");
    }

    @Override
    public Backend current() {
        return descriptor(store.name(), store.persistent(), running.getAsBoolean());
    }

    @Override
    public Collection<Backend> available() {
        return Arrays.stream(BackendType.values())
                .map(type -> descriptor(type.displayName(), type.persistent(), running.getAsBoolean() && type.displayName().equalsIgnoreCase(store.name())))
                .toList();
    }

    private static Backend descriptor(String name, boolean persistent, boolean online) {
        return new Description(name, persistent, online ? BackendStatus.ONLINE : BackendStatus.OFFLINE);
    }

    private record Description(String name, boolean persistent, BackendStatus status) implements Backend {}
}
