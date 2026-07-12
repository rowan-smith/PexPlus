package dev.rono.permissions.api.event;

import java.util.Objects;

public final class PermissionAddedEvent extends PermissionEvent {

    private final String holderType;
    private final String holderIdentifier;
    private final String permission;

    public PermissionAddedEvent(String holderType, String holderIdentifier, String permission) {
        this.holderType = Objects.requireNonNull(holderType);
        this.holderIdentifier = Objects.requireNonNull(holderIdentifier);
        this.permission = Objects.requireNonNull(permission);
    }

    public String holderType() {
        return holderType;
    }

    public String holderIdentifier() {
        return holderIdentifier;
    }

    public String permission() {
        return permission;
    }

}
