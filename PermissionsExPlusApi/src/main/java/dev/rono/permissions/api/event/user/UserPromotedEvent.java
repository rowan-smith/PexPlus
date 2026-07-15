package dev.rono.permissions.api.event.user;

import dev.rono.permissions.api.event.Event;
import dev.rono.permissions.api.ladder.PromotionResult;

public interface UserPromotedEvent extends Event {
    PromotionResult result();
}
