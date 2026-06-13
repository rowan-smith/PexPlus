package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.user.User;

/**
 * World-scoped view of a {@link User} (legacy {@link #world()} label over {@link UserContext}).
 */
public interface UserWorldContext extends UserContext {

    @Override
    User subject();

    /** @return legacy world/realm label for this context */
    String world();
}
