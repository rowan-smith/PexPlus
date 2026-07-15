package dev.rono.permissions.core.config;

import java.util.Locale;

public enum CacheFailureFallback {
    DENY,
    ALLOW;

    public static CacheFailureFallback parse(String value) {
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (RuntimeException error) {
            throw new IllegalArgumentException("cache.cache-failure-fallback must be deny or allow", error);
        }
    }
}
