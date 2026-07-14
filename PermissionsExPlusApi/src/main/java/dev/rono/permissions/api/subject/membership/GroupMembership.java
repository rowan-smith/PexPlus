package dev.rono.permissions.api.subject.membership;

import dev.rono.permissions.api.group.Group;

import java.time.Instant;
import java.util.Optional;

public interface GroupMembership {
    Group group();

    Instant created();

    Optional<Instant> expires();
}
