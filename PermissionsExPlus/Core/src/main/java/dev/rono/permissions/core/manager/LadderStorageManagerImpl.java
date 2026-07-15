package dev.rono.permissions.core.manager;

import dev.rono.permissions.api.ladder.Ladder;
import dev.rono.permissions.api.ladder.LadderStorageManager;
import dev.rono.permissions.api.util.Identifiers;
import dev.rono.permissions.core.model.LadderSnapshot;
import dev.rono.permissions.core.store.DataStore;
import dev.rono.permissions.core.store.SnapshotCodec;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

public final class LadderStorageManagerImpl implements LadderStorageManager {

    private static final String CATEGORY = "ladders";

    private final DataStore store;
    private final Executor executor;

    public LadderStorageManagerImpl(DataStore store, Executor executor) {
        this.store = Objects.requireNonNull(store, "store");
        this.executor = Objects.requireNonNull(executor, "executor");
    }

    @Override
    public CompletionStage<Optional<Ladder>> get(String name) {
        return Stages.call(() -> getNow(name).map(Ladder.class::cast), executor);
    }

    public CompletionStage<Set<String>> identifiers() {
        return Stages.call(() -> Set.copyOf(store.all(CATEGORY).keySet()), executor);
    }

    Optional<LadderSnapshot> getNow(String name) {
        return store.get(CATEGORY, Identifiers.ladder(name)).map(SnapshotCodec::ladder);
    }

    Map<String, LadderSnapshot> allNow() {
        var ladders = new LinkedHashMap<String, LadderSnapshot>();

        store.all(CATEGORY).forEach((key, value) -> {
            ladders.put(key, SnapshotCodec.ladder(value));
        });

        return Map.copyOf(ladders);
    }

    void saveNow(LadderSnapshot ladder) {
        store.put(CATEGORY, ladder.name(), SnapshotCodec.ladder(ladder));
    }

    boolean deleteNow(String name) {
        return store.remove(CATEGORY, Identifiers.ladder(name));
    }
}
