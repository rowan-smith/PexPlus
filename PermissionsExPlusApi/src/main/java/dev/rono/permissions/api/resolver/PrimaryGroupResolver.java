package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.user.User;

import java.util.Optional;

/** Resolves the primary group used by chat and platform integrations. */
public interface PrimaryGroupResolver {

    /**
     * Resolves by the highest-weight direct group, highest-weight effective group,
     * then the configured implicit default group when the user has no assigned
     * groups. Effective and default groups participate only when enabled in the
     * query options.
     */
    Optional<Group> resolve(User user, QueryOptions options);

    default Optional<Group> resolve(User user, ContextSet contexts) {
        return resolve(user, QueryOptions.builder().contexts(contexts).build());
    }
}
