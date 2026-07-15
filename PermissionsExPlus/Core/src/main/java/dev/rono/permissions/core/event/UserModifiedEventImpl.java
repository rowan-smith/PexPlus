package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.user.UserModifiedEvent;
import dev.rono.permissions.api.user.User;

public record UserModifiedEventImpl(User previous, User current) implements UserModifiedEvent {}
