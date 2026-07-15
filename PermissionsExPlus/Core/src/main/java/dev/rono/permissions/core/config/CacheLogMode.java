package dev.rono.permissions.core.config;

import java.util.Locale;

public enum CacheLogMode {
    TOTAL,
    INDIVIDUAL;

    public static CacheLogMode parse(String value) {
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (RuntimeException error) {
            throw new IllegalArgumentException("cache.log-cache-mode must be total or individual", error);
        }
    }
}
