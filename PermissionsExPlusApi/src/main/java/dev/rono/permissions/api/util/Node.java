package dev.rono.permissions.api.util;

import dev.rono.permissions.api.context.ContextSet;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public interface Node {

    ContextSet contexts();

    Optional<Instant> expiry();

    default boolean contextual() {
        return !contexts().isEmpty();
    }

    default boolean temporary() {
        return expiry().isPresent();
    }

    default boolean permanent() {
        return !temporary();
    }

    default boolean expired() {
        return expiredAt(Instant.now());
    }

    default boolean expiredAt(Instant instant) {
        Objects.requireNonNull(instant, "instant");

        return expiry()
                .map(expiry -> !expiry.isAfter(instant))
                .orElse(false);
    }
}
