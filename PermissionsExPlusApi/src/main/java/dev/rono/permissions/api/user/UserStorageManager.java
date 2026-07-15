package dev.rono.permissions.api.user;

import dev.rono.permissions.api.managers.StorageManager;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public interface UserStorageManager extends StorageManager<UUID, User> {

    CompletionStage<Optional<User>> get(String username);

    CompletionStage<Set<UUID>> identifiers();

    CompletionStage<Set<String>> names();
}
