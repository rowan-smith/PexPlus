package dev.rono.permissions.api.event;

import java.util.Objects;
import java.util.UUID;

public final class UserCreatedEvent extends PermissionEvent {

    private final UUID userId;

    public UserCreatedEvent(UUID userId) {
        this.userId = Objects.requireNonNull(userId);
    }

    public UUID userId() {
        return userId;
    }

}
