package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.group.GroupDeletedEvent;
import dev.rono.permissions.api.group.Group;

public record GroupDeletedEventImpl(Group group) implements GroupDeletedEvent {}
