package dev.rono.permissions.core.manager;

import dev.rono.permissions.api.user.User;
import dev.rono.permissions.api.user.UserStorageManager;
import dev.rono.permissions.api.util.Identifiers;
import dev.rono.permissions.core.model.UserSnapshot;
import dev.rono.permissions.core.store.DataStore;
import dev.rono.permissions.core.store.SnapshotCodec;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public final class UserStorageManagerImpl implements UserStorageManager {

    private static final String CATEGORY = "users";

    private final DataStore store;
    private final Executor executor;

    public UserStorageManagerImpl(DataStore store, Executor executor) {
        this.store = Objects.requireNonNull(store, "store");
        this.executor = Objects.requireNonNull(executor, "executor");
    }

    @Override
    public CompletionStage<Optional<User>> get(UUID id) {
        return Stages.call(() -> getNow(id).map(User.class::cast), executor);
    }

    @Override
    public CompletionStage<Optional<User>> get(String username) {
        return Stages.call(() -> getNow(username).map(User.class::cast), executor);
    }

    @Override
    public CompletionStage<Set<UUID>> identifiers() {
        return Stages.call(() -> store.all(CATEGORY)
                .keySet()
                .stream()
                .map(UUID::fromString)
                .collect(Collectors.toUnmodifiableSet()), executor);
    }

    @Override
    public CompletionStage<Set<String>> names() {
        return Stages.call(() -> store.all(CATEGORY)
                .values()
                .stream()
                .map(SnapshotCodec::user)
                .map(UserSnapshot::name)
                .collect(Collectors.toUnmodifiableSet()), executor);
    }

    Optional<UserSnapshot> getNow(UUID id) {
        return store.get(CATEGORY, id.toString()).map(SnapshotCodec::user);
    }

    Optional<UserSnapshot> getNow(String username) {
        var lookup = Identifiers.usernameLookup(username);

        return store.all(CATEGORY)
                .values()
                .stream()
                .map(SnapshotCodec::user)
                .filter(user -> Identifiers.usernameLookup(user.name()).equals(lookup))
                .findFirst();
    }

    Map<String, UserSnapshot> allNow() {
        return store.all(CATEGORY)
                .entrySet()
                .stream()
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, entry -> SnapshotCodec.user(entry.getValue())));
    }

    void saveNow(UserSnapshot user) {
        store.put(CATEGORY, user.uniqueId().toString(), SnapshotCodec.user(user));
    }

    boolean deleteNow(UUID id) {
        return store.remove(CATEGORY, id.toString());
    }
}
