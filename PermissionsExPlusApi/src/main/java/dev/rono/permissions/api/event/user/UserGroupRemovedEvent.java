package dev.rono.permissions.api.event.user;

import dev.rono.permissions.api.group.Group;

public interface UserGroupRemovedEvent extends UserEvent {
    Group group();
}
