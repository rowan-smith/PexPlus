package dev.rono.permissions.core.context;

import java.util.Map;
import java.util.UUID;

/**
 * Internal mutation boundary used by official platform shims.
 */
public interface CoreStateTracker {
    void updateState(UUID subject, String key, String value);

    void replaceState(UUID subject, Map<String, String> values);

    void clearState(UUID subject);
}
