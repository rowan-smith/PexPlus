package dev.rono.permissions.api.ladder;

import dev.rono.permissions.api.parent.ParentNode;
import dev.rono.permissions.api.user.User;

import java.util.Optional;

public interface PromotionResult {

    User user();

    Ladder ladder();

    Optional<ParentNode> previousMembership();

    Optional<ParentNode> currentMembership();

    default Optional<String> oldGroup() {
        return previousMembership().map(ParentNode::group);
    }

    default Optional<String> newGroup() {
        return currentMembership().map(ParentNode::group);
    }

    PromotionStatus status();
}
