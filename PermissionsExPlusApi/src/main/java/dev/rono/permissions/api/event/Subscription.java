package dev.rono.permissions.api.event;

public interface Subscription extends AutoCloseable {

    void unsubscribe();

    @Override
    default void close() {
        unsubscribe();
    }
}
