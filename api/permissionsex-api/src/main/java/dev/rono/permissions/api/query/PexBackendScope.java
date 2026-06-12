package dev.rono.permissions.api.query;

import dev.rono.permissions.api.PexPermissionsExException;
import dev.rono.permissions.api.backend.PexBackendHandle;
import dev.rono.permissions.api.backend.PexBackendInfo;
import dev.rono.permissions.api.data.PexImportMode;
import dev.rono.permissions.api.service.PexPermissionServiceBridge;

/**
 * Backend administration — obtain via {@link dev.rono.permissions.api.service.PexPermissionService#backend()}.
 *
 * <pre>{@code
 * pex.backend().getActive();
 * pex.backend().activate("sql");
 * pex.backend().exportData();
 * }</pre>
 */
public final class PexBackendScope {

    private final PexPermissionServiceBridge service;

    public PexBackendScope(PexPermissionServiceBridge service) {
        this.service = service;
    }

    /**
     * Returns a snapshot of the currently active permission backend.
     *
     * @return active backend metadata
     */
    public PexBackendInfo getActive() {
        return service.activeBackend();
    }

    /**
     * Reports whether a backend is currently active.
     *
     * @return {@code true} when an active backend is configured
     */
    public boolean isActive() {
        return service.activeBackend() != null;
    }

    /**
     * Reports whether the given alias matches the active backend type.
     *
     * @param alias configured backend alias to compare
     * @return {@code true} when {@code alias} equals the active backend type
     */
    public boolean isActive(String alias) {
        return alias != null && alias.equals(getActive().type());
    }

    /**
     * Returns the configured backend type identifier (alias) of the active backend.
     *
     * @return backend type string from {@link PexBackendInfo#type()}
     */
    public String type() {
        return getActive().type();
    }

    /**
     * Returns the simple class name of the active backend implementation.
     *
     * @return runtime implementation simple name from {@link PexBackendInfo#simpleName()}
     */
    public String simpleName() {
        return getActive().simpleName();
    }

    /**
     * Returns a human-readable label suitable for logs and diagnostics.
     *
     * @return diagnostic label from {@link PexBackendInfo#diagnosticLabel()}
     */
    public String diagnosticLabel() {
        return getActive().diagnosticLabel();
    }

    /**
     * Switches the active backend to the given configured alias.
     *
     * @param alias configured backend alias (for example {@code "file"} or {@code "sql"})
     * @throws PexPermissionsExException if the alias is unknown or activation fails
     */
    public void activate(String alias) throws PexPermissionsExException {
        service.setActiveBackend(alias);
    }

    /**
     * Opens a handle to a non-active backend for inspection or data transfer.
     *
     * @param alias configured backend alias
     * @return a {@link PexBackendHandle} for the requested backend
     * @throws PexPermissionsExException if the alias is unknown or the handle cannot be created
     */
    public PexBackendHandle createHandle(String alias) throws PexPermissionsExException {
        return service.createBackendHandle(alias);
    }

    /**
     * Replaces active-backend data with the contents of another configured backend.
     *
     * @param backendAlias source backend alias to import from
     * @throws PexPermissionsExException if the alias is unknown or import fails
     */
    public void importFrom(String backendAlias) throws PexPermissionsExException {
        service.importFromBackend(backendAlias);
    }

    /**
     * Serializes all users, groups, and world inheritance from the active backend.
     *
     * @return exported document (format depends on the active backend)
     * @throws PexPermissionsExException if export fails
     */
    public String exportData() throws PexPermissionsExException {
        return service.exportData();
    }

    /**
     * Merges or replaces active-backend data from a serialized document.
     *
     * @param document serialized permission data
     * @param mode merge strategy ({@link PexImportMode#MERGE} or {@link PexImportMode#REPLACE})
     * @throws PexPermissionsExException if the document is invalid or import fails
     */
    public void importData(String document, PexImportMode mode) throws PexPermissionsExException {
        service.importData(document, mode);
    }
}
