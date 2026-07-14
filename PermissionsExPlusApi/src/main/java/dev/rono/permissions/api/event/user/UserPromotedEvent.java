package dev.rono.permissions.api.event.user;

import dev.rono.permissions.api.ladder.Ladder;

public interface UserPromotedEvent extends UserEvent {
    Ladder ladder();
}