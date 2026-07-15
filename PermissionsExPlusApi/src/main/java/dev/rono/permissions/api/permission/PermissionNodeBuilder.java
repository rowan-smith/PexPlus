package dev.rono.permissions.api.permission;

import dev.rono.permissions.api.context.ContextSet;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Builds an immutable permission node.
 *
 * <p>
 * New builders default to {@link PermissionValue#ALLOW}, an empty context
 * set, and no expiry.
 * </p>
 */
public interface PermissionNodeBuilder {

    PermissionNodeBuilder permission(String permission);

    PermissionNodeBuilder value(PermissionValue value);

    PermissionNodeBuilder contexts(ContextSet contexts);

    PermissionNodeBuilder expiry(Instant expiry);

    PermissionNodeBuilder permanent();

    default PermissionNodeBuilder duration(Duration duration) {
        Objects.requireNonNull(duration, "duration");

        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("duration must be positive");
        }

        return expiry(Instant.now().plus(duration));
    }

    PermissionNode build();
}
