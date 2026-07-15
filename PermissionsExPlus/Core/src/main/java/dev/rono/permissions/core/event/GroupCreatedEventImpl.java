package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.group.GroupCreatedEvent;
import dev.rono.permissions.api.group.Group;

public record GroupCreatedEventImpl(Group group) implements GroupCreatedEvent {}
