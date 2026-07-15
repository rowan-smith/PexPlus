package dev.rono.permissions.core.logger;

import dev.rono.permissions.core.platform.PlatformLogger;

import java.util.function.BooleanSupplier;

/**
 * Consistent opt-in diagnostic logging; never emits sensitive configuration
 * values.
 */
public final class DebugLogger {
    private final PlatformLogger logger;
    private final BooleanSupplier enabled;

    public DebugLogger(PlatformLogger logger, BooleanSupplier enabled) {
        this.logger = logger;
        this.enabled = enabled;
    }

    public void log(String component, String message) {
        if (enabled.getAsBoolean()) {
            logger.info("[" + component + "] " + message);
        }
    }

    /**
     * Security and configuration warnings are intentionally not gated by debug
     * mode.
     */
    public void warn(String component, String message) {
        logger.warn("[" + component + "] " + message);
    }
}
