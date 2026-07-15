package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.user.UserDeletedEvent;
import dev.rono.permissions.api.user.User;

public record UserDeletedEventImpl(User user) implements UserDeletedEvent {}
