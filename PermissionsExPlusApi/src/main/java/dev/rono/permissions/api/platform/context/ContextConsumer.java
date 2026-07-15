package dev.rono.permissions.api.platform.context;

import dev.rono.permissions.api.context.ContextSet;

/** Receives context entries contributed by a platform calculator. */
@FunctionalInterface
public interface ContextConsumer {

    void accept(String key, String value);

    default void accept(ContextSet contexts) {
        contexts.asMap().forEach((key, values) -> values.forEach(value -> accept(key, value)));
    }
}
