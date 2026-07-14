package dev.rono.permissions.api.event.ladder;

import dev.rono.permissions.api.event.PermissionEvent;
import dev.rono.permissions.api.ladder.Ladder;

public interface LadderEvent extends PermissionEvent {
    Ladder ladder();
}
