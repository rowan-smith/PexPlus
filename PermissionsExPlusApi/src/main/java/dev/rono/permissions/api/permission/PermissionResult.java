package dev.rono.permissions.api.permission;

/** The effective result of resolving a permission. */
public enum PermissionResult {
    ALLOW,
    DENY,
    UNDEFINED;

    public boolean allowed() {
        return this == ALLOW;
    }

    public boolean denied() {
        return this == DENY;
    }

    public boolean defined() {
        return this != UNDEFINED;
    }
}
