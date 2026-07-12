package dev.rono.permissions.api.event;

import java.util.Objects;
import java.util.UUID;

public final class UserGroupAddedEvent extends PermissionEvent {

    private final UUID userId;
    private final String groupName;

    public UserGroupAddedEvent(UUID userId, String groupName) {
        this.userId = Objects.requireNonNull(userId);
        this.groupName = Objects.requireNonNull(groupName);
    }

    public UUID userId() {
        return userId;
    }

    public String groupName() {
        return groupName;
    }

}
