package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.group.Group;

/**
 * Server-scoped view of a {@link Group}.
 *
 * <p>Every method applies to the bound server from {@link #server()} (see {@link SubjectServerContext}).
 * On proxy runtimes, {@code server} is a backend id from the proxy's registered servers.</p>
 */
public interface GroupServerContext extends GroupWorldContext, SubjectServerContext {

    /**
     * Returns the underlying group.
     *
     * @return the group this context wraps
     */
    @Override
    Group subject();
}
