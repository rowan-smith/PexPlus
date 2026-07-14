package dev.rono.permissions.api.event.user;

import dev.rono.permissions.api.event.CancellableEvent;
import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.ladder.Ladder;

public interface UserDemotingEvent extends UserEvent, CancellableEvent {
    Ladder ladder();

    Group from();

    Group to();
}