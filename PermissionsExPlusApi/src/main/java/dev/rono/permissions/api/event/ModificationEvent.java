package dev.rono.permissions.api.event;

/** An immutable before-and-after snapshot of a successful modification. */
public interface ModificationEvent<T> extends Event {

    T previous();

    T current();
}
