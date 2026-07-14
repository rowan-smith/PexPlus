package dev.rono.permissions.api.event.group;

import dev.rono.permissions.api.group.Group;

public interface GroupParentRemovedEvent extends GroupEvent {
    Group parent();
}
