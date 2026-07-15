package dev.rono.permissions.api.event.group;

import dev.rono.permissions.api.event.Event;
import dev.rono.permissions.api.group.Group;

public interface GroupCreatedEvent extends Event {
    Group group();
}
