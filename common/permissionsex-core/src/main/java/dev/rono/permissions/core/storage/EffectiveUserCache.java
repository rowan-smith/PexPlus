package dev.rono.permissions.core.storage;

import dev.rono.permissions.api.permission.PermissionContext;
import dev.rono.permissions.core.storage.model.Group;
import dev.rono.permissions.core.storage.model.Ladder;
import dev.rono.permissions.core.storage.model.User;
import dev.rono.permissions.core.storage.resolution.EffectiveUser;
import dev.rono.permissions.core.storage.resolution.PermissionResolver;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Caches resolved {@link EffectiveUser} snapshots and invalidates on data mutations.
 */
public final class EffectiveUserCache {

    private final ConcurrentHashMap<CacheKey, EffectiveUser> cache = new ConcurrentHashMap<>();
    private volatile long generation;

    public EffectiveUser getOrResolve(UUID userId,
                                    PermissionContext context,
                                    Supplier<User> userLoader,
                                    Supplier<Map<Integer, Group>> groupsLoader,
                                    Supplier<List<Ladder>> laddersLoader) {
        CacheKey key = new CacheKey(userId, ContextKeyCodec.encode(context), generation);
        return cache.computeIfAbsent(key, ignored -> PermissionResolver.resolve(
                userLoader.get(),
                groupsLoader.get(),
                laddersLoader.get(),
                context,
                Instant.now()));
    }

    public void invalidateUser(UUID userId) {
        generation++;
        cache.keySet().removeIf(key -> key.userId().equals(userId));
    }

    public void invalidateGroup(int groupId) {
        generation++;
        cache.clear();
    }

    public void invalidateAll() {
        generation++;
        cache.clear();
    }

    public int size() {
        return cache.size();
    }

    public long generation() {
        return generation;
    }

    private record CacheKey(UUID userId, String contextKey, long generation) {}
}
