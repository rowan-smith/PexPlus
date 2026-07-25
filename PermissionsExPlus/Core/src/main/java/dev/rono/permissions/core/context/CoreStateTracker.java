package dev.rono.permissions.core.context;

import dev.rono.permissions.api.context.ContextKeys;

import java.util.Map;
import java.util.UUID;

/**
 * Internal mutation boundary used by official platform shims.
 */
public interface CoreStateTracker {
    void updateState(UUID subject, String key, String value);

    default void updateState(UUID subject, ContextKeys key, String value) {
        updateState(subject, key.key(), value);
    }

    void replaceState(UUID subject, Map<String, String> values);

    void clearState(UUID subject);
}
