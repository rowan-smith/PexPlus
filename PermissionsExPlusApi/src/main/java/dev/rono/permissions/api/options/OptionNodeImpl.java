package dev.rono.permissions.api.options;

import dev.rono.permissions.api.context.ContextSet;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

record OptionNodeImpl(
        String key,
        String value,
        ContextSet contexts,
        Optional<Instant> expiry) implements OptionNode {

    OptionNodeImpl {
        Objects.requireNonNull(key, "key");

        Objects.requireNonNull(value, "value");

        Objects.requireNonNull(contexts, "contexts");

        Objects.requireNonNull(expiry, "expiry");

        if (key.isBlank()) {
            throw new IllegalArgumentException("key cannot be blank");
        }

        if (value.isBlank()) {
            throw new IllegalArgumentException("value cannot be blank");
        }
    }
}
