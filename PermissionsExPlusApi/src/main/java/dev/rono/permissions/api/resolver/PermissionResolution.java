package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.permission.PermissionResult;

import java.util.List;
import java.util.Optional;

/** An immutable explanation of an effective permission decision. */
public interface PermissionResolution {

    PermissionResult result();

    String requestedPermission();

    /** Returns the candidate whose status is {@link CandidateStatus#WINNER}. */
    Optional<ResolutionCandidate> winner();

    default Optional<PermissionNode> winningNode() {
        return winner().map(ResolutionCandidate::node);
    }

    default Optional<PermissionHolder> source() {
        return winner().map(ResolutionCandidate::source);
    }

    List<ResolutionCandidate> candidates();
}
