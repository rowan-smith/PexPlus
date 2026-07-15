package dev.rono.permissions.api.parent;

import dev.rono.permissions.api.context.ContextSet;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

record ParentNodeImpl(String group, ContextSet contexts, Optional<Instant> expiry) implements ParentNode {

    ParentNodeImpl {
        Objects.requireNonNull(group, "group");

        Objects.requireNonNull(contexts, "contexts");

        Objects.requireNonNull(expiry, "expiry");

        if (group.isBlank()) {
            throw new IllegalArgumentException("group cannot be blank");
        }
    }
}
