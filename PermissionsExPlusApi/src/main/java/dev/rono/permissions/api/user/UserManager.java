package dev.rono.permissions.api.user;

import dev.rono.permissions.api.managers.Manager;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public interface UserManager extends Manager<UUID, User, UserModifier> {

    @Override
    UserCacheManager cache();

    @Override
    UserStorageManager storage();

    CompletionStage<User> modify(String username, Consumer<UserModifier> action);

    default CompletionStage<User> modify(User user, Consumer<UserModifier> action) {
        Objects.requireNonNull(user, "user");
        Objects.requireNonNull(action, "action");

        return modify(user.uniqueId(), action);
    }

    CompletionStage<Optional<User>> find(String username);
}
