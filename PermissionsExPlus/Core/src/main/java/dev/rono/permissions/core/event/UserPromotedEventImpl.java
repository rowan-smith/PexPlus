package dev.rono.permissions.core.event;

import dev.rono.permissions.api.event.user.UserPromotedEvent;
import dev.rono.permissions.api.ladder.PromotionResult;

public record UserPromotedEventImpl(PromotionResult result) implements UserPromotedEvent {}
