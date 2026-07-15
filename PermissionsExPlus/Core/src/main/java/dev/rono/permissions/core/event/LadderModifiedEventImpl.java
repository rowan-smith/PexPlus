package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.ladder.LadderModifiedEvent;
import dev.rono.permissions.api.ladder.Ladder;

public record LadderModifiedEventImpl(Ladder previous, Ladder current) implements LadderModifiedEvent {}
