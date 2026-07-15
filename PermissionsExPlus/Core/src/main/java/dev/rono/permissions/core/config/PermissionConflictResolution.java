package dev.rono.permissions.core.config;

import java.util.Locale;

/** Policy used when equally ranked permission candidates disagree. */
public enum PermissionConflictResolution {
    TRUE_WINS,
    DENY_WINS,
    STRICT;

    public static PermissionConflictResolution parse(String value) {
        try {
            return valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (RuntimeException error) {
            throw new IllegalArgumentException("inheritance.conflict-resolution must be true_wins, deny_wins, or strict", error);
        }
    }
}
