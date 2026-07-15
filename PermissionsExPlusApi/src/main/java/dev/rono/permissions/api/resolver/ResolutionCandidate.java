package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionNode;

import java.util.Optional;

/** A considered permission node and the factors affecting its precedence. */
public interface ResolutionCandidate {

    PermissionNode node();

    PermissionHolder source();

    int inheritanceDistance();

    int contextSpecificity();

    CandidateStatus status();

    default boolean applicable() {
        return status() == CandidateStatus.WINNER || status() == CandidateStatus.OUTRANKED;
    }

    Optional<String> detail();
}
