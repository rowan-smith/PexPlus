package dev.rono.permissions.api.event.user;

import dev.rono.permissions.api.group.Group;

public interface UserGroupAddedEvent extends UserEvent {
    Group group();
}
