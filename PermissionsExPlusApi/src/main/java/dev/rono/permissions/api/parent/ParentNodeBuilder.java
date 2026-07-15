package dev.rono.permissions.api.parent;

import dev.rono.permissions.api.context.ContextSet;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * Builds an immutable parent node.
 *
 * <p>
 * New builders default to an empty context set and no expiry.
 * </p>
 */
public interface ParentNodeBuilder {

    ParentNodeBuilder group(String group);

    ParentNodeBuilder contexts(ContextSet contexts);

    ParentNodeBuilder expiry(Instant expiry);

    ParentNodeBuilder permanent();

    default ParentNodeBuilder duration(Duration duration) {
        Objects.requireNonNull(duration, "duration");

        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("duration must be positive");
        }

        return expiry(Instant.now().plus(duration));
    }

    ParentNode build();
}
