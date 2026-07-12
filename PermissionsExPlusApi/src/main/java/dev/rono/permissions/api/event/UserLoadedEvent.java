package dev.rono.permissions.api.event;

import java.util.Objects;
import java.util.UUID;

public final class UserLoadedEvent extends PermissionEvent {

    private final UUID userId;

    public UserLoadedEvent(UUID userId) {
        this.userId = Objects.requireNonNull(userId);
    }

    public UUID userId() {
        return userId;
    }

}
