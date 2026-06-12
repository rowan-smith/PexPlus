package dev.rono.permissions.api.query;

import dev.rono.permissions.api.PermissionsExException;
import dev.rono.permissions.api.backend.BackendHandle;
import dev.rono.permissions.api.backend.BackendInfo;
import dev.rono.permissions.api.data.ImportMode;
import dev.rono.permissions.api.service.PermissionServiceBridge;

/**
 * Backend administration — obtain via {@link PermissionQuery#backend()}.
 *
 * <pre>{@code
 * pex.query().backend().info();
 * pex.query().backend().activate("sql");
 * pex.query().backend().exportData();
 * }</pre>
 */
public final class BackendScope {

    private final PermissionServiceBridge service;

    BackendScope(PermissionServiceBridge service) {
        this.service = service;
    }

    /**
     * Returns a snapshot of the currently active permission backend.
     *
     * @return active backend metadata
     */
    public BackendInfo info() {
        return service.backend();
    }

    /**
     * Returns the configured backend type identifier (alias) of the active backend.
     *
     * @return backend type string from {@link BackendInfo#type()}
     */
    public String type() {
        return info().type();
    }

    /**
     * Returns the simple class name of the active backend implementation.
     *
     * @return runtime implementation simple name from {@link BackendInfo#simpleName()}
     */
    public String simpleName() {
        return info().simpleName();
    }

    /**
     * Returns a human-readable label suitable for logs and diagnostics.
     *
     * @return diagnostic label from {@link BackendInfo#diagnosticLabel()}
     */
    public String diagnosticLabel() {
        return info().diagnosticLabel();
    }

    /**
     * Switches the active backend to the given configured alias.
     *
     * @param alias configured backend alias (for example {@code "file"} or {@code "sql"})
     * @throws PermissionsExException if the alias is unknown or activation fails
     */
    public void activate(String alias) throws PermissionsExException {
        service.setActiveBackend(alias);
    }

    /**
     * Opens a handle to a non-active backend for inspection or data transfer.
     *
     * @param alias configured backend alias
     * @return a {@link BackendHandle} for the requested backend
     * @throws PermissionsExException if the alias is unknown or the handle cannot be created
     */
    public BackendHandle createHandle(String alias) throws PermissionsExException {
        return service.createBackendHandle(alias);
    }

    /**
     * Replaces active-backend data with the contents of another configured backend.
     *
     * @param backendAlias source backend alias to import from
     * @throws PermissionsExException if the alias is unknown or import fails
     */
    public void importFrom(String backendAlias) throws PermissionsExException {
        service.importFromBackend(backendAlias);
    }

    /**
     * Serializes all users, groups, and world inheritance from the active backend.
     *
     * @return exported document (format depends on the active backend)
     * @throws PermissionsExException if export fails
     */
    public String exportData() throws PermissionsExException {
        return service.exportData();
    }

    /**
     * Merges or replaces active-backend data from a serialized document.
     *
     * @param document serialized permission data
     * @param mode merge strategy ({@link ImportMode#MERGE} or {@link ImportMode#REPLACE})
     * @throws PermissionsExException if the document is invalid or import fails
     */
    public void importData(String document, ImportMode mode) throws PermissionsExException {
        service.importData(document, mode);
    }
}
