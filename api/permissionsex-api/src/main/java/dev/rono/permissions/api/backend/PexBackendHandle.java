package dev.rono.permissions.api.backend;

import dev.rono.permissions.api.PexPermissionsExException;

/**
 * Non-active backend instance for inspection and data transfer.
 *
 * <p>Created via {@link dev.rono.permissions.api.query.PexBackendScope#createHandle(String)} or
 * {@link dev.rono.permissions.api.service.PexPermissionServiceBridge#createBackendHandle(String)}.
 * The handle targets a configured backend without making it active.</p>
 */
public interface PexBackendHandle {

    /**
     * Returns metadata describing this backend instance.
     *
     * @return backend type, implementation name, and diagnostic label
     */
    PexBackendInfo info();

    /**
     * Copies all users, groups, and world inheritance from the active backend into this backend.
     *
     * @throws PexPermissionsExException if the copy operation fails
     */
    void copyFromActive() throws PexPermissionsExException;

    /**
     * Replaces active-backend data with the contents of this backend.
     *
     * <p>Uses the merge semantics of the underlying {@code loadFrom} implementation.</p>
     *
     * @throws PexPermissionsExException if applying data to the active backend fails
     */
    void applyToActive() throws PexPermissionsExException;
}
