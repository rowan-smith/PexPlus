package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.ladder.LadderCreatedEvent;
import dev.rono.permissions.api.ladder.Ladder;

public record LadderCreatedEventImpl(Ladder ladder) implements LadderCreatedEvent {}
