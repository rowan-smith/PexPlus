package dev.rono.permissions.api.event.user;

import dev.rono.permissions.api.event.Event;
import dev.rono.permissions.api.user.User;

public interface UserDeletedEvent extends Event {
    User user();
}
