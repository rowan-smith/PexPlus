package dev.rono.permissions.api.resolver;

/** Typed outcome for a permission candidate considered during resolution. */
public enum CandidateStatus {
    WINNER,
    OUTRANKED,
    CONFLICT,
    EXPIRED,
    CONTEXT_MISMATCH,
    PERMISSION_MISMATCH,
    INHERITANCE_DISABLED,
    DEFAULTS_DISABLED
}
