package dev.rono.permissions.api.event;

import java.util.Objects;

public final class GroupCreatedEvent extends PermissionEvent {

    private final String groupName;

    public GroupCreatedEvent(String groupName) {
        this.groupName = Objects.requireNonNull(groupName);
    }

    public String groupName() {
        return groupName;
    }

}
