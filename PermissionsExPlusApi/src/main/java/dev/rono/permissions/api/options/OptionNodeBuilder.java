package dev.rono.permissions.api.options;

import dev.rono.permissions.api.context.ContextSet;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public interface OptionNodeBuilder {

    OptionNodeBuilder option(String key, String value);

    OptionNodeBuilder key(String key);

    OptionNodeBuilder value(String value);

    OptionNodeBuilder contexts(ContextSet contexts);

    OptionNodeBuilder expiry(Instant expiry);

    OptionNodeBuilder permanent();

    default OptionNodeBuilder duration(Duration duration) {
        Objects.requireNonNull(duration, "duration");

        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("duration must be positive");
        }

        return expiry(Instant.now().plus(duration));
    }

    OptionNode build();
}
