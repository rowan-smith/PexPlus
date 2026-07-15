package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.ladder.LadderDeletedEvent;
import dev.rono.permissions.api.ladder.Ladder;

public record LadderDeletedEventImpl(Ladder ladder) implements LadderDeletedEvent {}
