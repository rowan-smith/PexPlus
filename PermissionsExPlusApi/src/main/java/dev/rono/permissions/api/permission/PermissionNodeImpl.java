package dev.rono.permissions.api.permission;

import dev.rono.permissions.api.context.ContextSet;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

record PermissionNodeImpl(String permission, PermissionValue value, ContextSet contexts,
        Optional<Instant> expiry) implements PermissionNode {

    PermissionNodeImpl {
        Objects.requireNonNull(permission, "permission");

        Objects.requireNonNull(value, "value");

        Objects.requireNonNull(contexts, "contexts");

        Objects.requireNonNull(expiry, "expiry");

        if (permission.isBlank()) {
            throw new IllegalArgumentException("permission cannot be blank");
        }
    }
}
