package dev.rono.permissions.api.event;

import java.util.Objects;

public final class GroupDeletedEvent extends PermissionEvent {

    private final String groupName;

    public GroupDeletedEvent(String groupName) {
        this.groupName = Objects.requireNonNull(groupName);
    }

    public String groupName() {
        return groupName;
    }

}
