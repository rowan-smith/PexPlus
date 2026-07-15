package dev.rono.permissions.core.config;

import java.util.Locale;

/** Controls how prefix and suffix options from multiple groups are combined. */
public enum MetaFormatting {
    HIGHEST_WEIGHT,
    ACCUMULATED;

    public static MetaFormatting parse(String value) {
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (RuntimeException error) {
            throw new IllegalArgumentException("inheritance.meta-formatting must be highest_weight or accumulated", error);
        }
    }
}
