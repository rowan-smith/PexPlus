package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.group.GroupModifiedEvent;
import dev.rono.permissions.api.group.Group;

public record GroupModifiedEventImpl(Group previous, Group current) implements GroupModifiedEvent {}
