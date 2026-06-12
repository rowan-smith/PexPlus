package dev.rono.permissions.api.query;

import dev.rono.permissions.api.service.PexPermissionServiceBridge;
import java.util.Set;

/**
 * PexUser registry — obtain via {@link dev.rono.permissions.api.service.PexPermissionService#users()}.
 *
 * <p>For subject lookup, use top-level {@link dev.rono.permissions.api.service.PexPermissionService#user(String)}
 * (resolve/materialize) or {@link dev.rono.permissions.api.service.PexPermissionService#findUser(String)}
 * (persisted only).</p>
 */
public final class PexUsersScope {

    private final PexPermissionServiceBridge service;

    public PexUsersScope(PexPermissionServiceBridge service) {
        this.service = service;
    }

    /**
     * Returns the number of user records in the active backend.
     *
     * @return persisted user count
     */
    public int count() {
        return service.userCount();
    }

    /**
     * Returns every user identifier stored in the active backend.
     *
     * @return set of persisted user identifiers
     */
    public Set<String> identifiers() {
        return service.userIdentifiers();
    }

    /**
     * Removes a user record from the active backend.
     *
     * @param identifier user name or UUID string to delete
     */
    public void delete(String identifier) {
        service.deleteUser(identifier);
    }
}
