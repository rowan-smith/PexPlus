package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.user.UserDemotedEvent;
import dev.rono.permissions.api.ladder.PromotionResult;

public record UserDemotedEventImpl(PromotionResult result) implements UserDemotedEvent {}
