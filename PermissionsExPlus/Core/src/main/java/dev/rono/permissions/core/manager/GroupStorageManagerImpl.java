package dev.rono.permissions.core.manager;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.group.GroupStorageManager;
import dev.rono.permissions.api.util.Identifiers;
import dev.rono.permissions.core.model.GroupSnapshot;
import dev.rono.permissions.core.store.DataStore;
import dev.rono.permissions.core.store.SnapshotCodec;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

public final class GroupStorageManagerImpl implements GroupStorageManager {

    private static final String CATEGORY = "groups";

    private final DataStore store;
    private final Executor executor;

    public GroupStorageManagerImpl(DataStore store, Executor executor) {
        this.store = Objects.requireNonNull(store, "store");
        this.executor = Objects.requireNonNull(executor, "executor");
    }

    @Override
    public CompletionStage<Optional<Group>> get(String name) {
        return Stages.call(() -> getNow(name).map(Group.class::cast), executor);
    }

    public CompletionStage<Set<String>> identifiers() {
        return Stages.call(() -> Set.copyOf(store.all(CATEGORY).keySet()), executor);
    }

    Optional<GroupSnapshot> getNow(String name) {
        var key = Identifiers.group(name);

        return store.get(CATEGORY, key).map(SnapshotCodec::group);
    }

    Map<String, GroupSnapshot> allNow() {
        var groups = new LinkedHashMap<String, GroupSnapshot>();

        store.all(CATEGORY).forEach((key, value) -> groups.put(key, SnapshotCodec.group(value)));

        return Map.copyOf(groups);
    }

    void saveNow(GroupSnapshot group) {
        store.put(CATEGORY, group.name(), SnapshotCodec.group(group));
    }

    boolean deleteNow(String name) {
        return store.remove(CATEGORY, Identifiers.group(name));
    }
}
