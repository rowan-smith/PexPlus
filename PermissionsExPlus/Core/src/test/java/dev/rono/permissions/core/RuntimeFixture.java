package dev.rono.permissions.core;

import dev.rono.permissions.core.event.EventBusImpl;
import dev.rono.permissions.core.manager.GroupManagerImpl;
import dev.rono.permissions.core.manager.LadderManagerImpl;
import dev.rono.permissions.core.manager.UserManagerImpl;
import dev.rono.permissions.core.resolver.ResolverImpl;
import dev.rono.permissions.core.store.MemoryDataStore;

final class RuntimeFixture {
    final MemoryDataStore store = new MemoryDataStore();

    final EventBusImpl events = new EventBusImpl(error -> {
        throw new AssertionError(error);
    });

    final GroupManagerImpl groups = new GroupManagerImpl(store, events, 10);
    final UserManagerImpl users = new UserManagerImpl(store, events);
    final LadderManagerImpl ladders = new LadderManagerImpl(store, events);
    final ResolverImpl resolvers = new ResolverImpl(groups, 10);

    RuntimeFixture() {
        store.open();

        groups.attach(users, ladders);
        users.attachGroups(groups);
        ladders.attach(users, groups);
    }

    static <T> T await(java.util.concurrent.CompletionStage<T> stage) {
        return stage.toCompletableFuture().join();
    }
}
