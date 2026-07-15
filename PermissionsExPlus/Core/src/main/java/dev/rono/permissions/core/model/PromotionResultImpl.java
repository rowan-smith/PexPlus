package dev.rono.permissions.core.model;

import dev.rono.permissions.api.ladder.Ladder;
import dev.rono.permissions.api.ladder.PromotionResult;
import dev.rono.permissions.api.ladder.PromotionStatus;
import dev.rono.permissions.api.parent.ParentNode;
import dev.rono.permissions.api.user.User;

import java.util.Optional;

public record PromotionResultImpl(User user, Ladder ladder, Optional<ParentNode> previousMembership, Optional<ParentNode> currentMembership, PromotionStatus status) implements PromotionResult {}
