package dev.rono.permissions.api.platform.context;

/** A removable context-calculator registration. */
public interface ContextRegistration extends AutoCloseable {

    void unregister();

    @Override
    default void close() {
        unregister();
    }
}
