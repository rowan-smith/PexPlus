package dev.rono.permissions.core.config;

import java.util.Locale;

/** Controls whether expiry cleanup is logged as a summary or per node. */
public enum ExpiryLogMode {
    TOTAL,
    INDIVIDUAL;

    public static ExpiryLogMode parse(String value) {
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (RuntimeException error) {
            throw new IllegalArgumentException("temporary-permissions.log-expiry-mode must be total or individual",
                    error);
        }
    }
}
