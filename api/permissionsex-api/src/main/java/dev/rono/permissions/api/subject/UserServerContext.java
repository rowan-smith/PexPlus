package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.user.User;

/**
 * Server-scoped view of a {@link User}.
 *
 * <p>Every method applies to the bound server from {@link #server()} (see {@link SubjectServerContext}).
 * On proxy runtimes, {@code server} is the connected backend id; there is no separate world dimension
 * on the proxy — use this context (or {@link #inWorld(String)} with the same name) to grant permissions
 * per backend.</p>
 */
public interface UserServerContext extends UserWorldContext, SubjectServerContext {

    /**
     * Returns the underlying user.
     *
     * @return the user this context wraps
     */
    @Override
    User subject();
}
