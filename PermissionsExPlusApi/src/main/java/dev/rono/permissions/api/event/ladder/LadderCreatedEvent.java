package dev.rono.permissions.api.event.ladder;

import dev.rono.permissions.api.event.Event;
import dev.rono.permissions.api.ladder.Ladder;

public interface LadderCreatedEvent extends Event {
    Ladder ladder();
}
