package dev.rono.permissions.api.event;

import java.util.Objects;
import java.util.UUID;

public final class UserUnloadedEvent extends PermissionEvent {

    private final UUID userId;

    public UserUnloadedEvent(UUID userId) {
        this.userId = Objects.requireNonNull(userId);
    }

    public UUID userId() {
        return userId;
    }

}
