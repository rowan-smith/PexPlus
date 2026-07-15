package dev.rono.permissions.api.ladder;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.managers.Manager;
import dev.rono.permissions.api.user.User;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public interface LadderManager extends Manager<String, Ladder, LadderModifier> {

    @Override
    LadderCacheManager cache();

    @Override
    LadderStorageManager storage();

    default CompletionStage<Ladder> modify(Ladder ladder, Consumer<LadderModifier> action) {
        Objects.requireNonNull(ladder, "ladder");
        Objects.requireNonNull(action, "action");

        return modify(ladder.name(), action);
    }

    CompletionStage<PromotionResult> promote(String name, String ladder);

    default CompletionStage<PromotionResult> promote(UUID uniqueId, String ladder) {
        return promote(uniqueId, ladder, ContextSet.empty());
    }

    CompletionStage<PromotionResult> promote(UUID uniqueId, String ladder, ContextSet contexts);

    default CompletionStage<PromotionResult> promote(User user, String ladder) {
        Objects.requireNonNull(user, "user");

        Objects.requireNonNull(ladder, "ladder");

        return promote(user.uniqueId(), ladder);
    }

    default CompletionStage<PromotionResult> promote(User user, String ladder, ContextSet contexts) {
        Objects.requireNonNull(user, "user");

        return promote(user.uniqueId(), ladder, contexts);
    }

    CompletionStage<PromotionResult> demote(String name, String ladder);

    default CompletionStage<PromotionResult> demote(UUID uniqueId, String ladder) {
        return demote(uniqueId, ladder, ContextSet.empty());
    }

    CompletionStage<PromotionResult> demote(UUID uniqueId, String ladder, ContextSet contexts);

    default CompletionStage<PromotionResult> demote(User user, String ladder) {
        Objects.requireNonNull(user, "user");

        Objects.requireNonNull(ladder, "ladder");

        return demote(user.uniqueId(), ladder);
    }

    default CompletionStage<PromotionResult> demote(User user, String ladder, ContextSet contexts) {
        Objects.requireNonNull(user, "user");

        return demote(user.uniqueId(), ladder, contexts);
    }
}
