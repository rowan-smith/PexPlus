package dev.rono.permissions.api.event;


public interface CancellableEvent {
    boolean isCancelled();

    void setCancelled(boolean cancelled);

    default void cancel() {
        setCancelled(true);
    }
}
