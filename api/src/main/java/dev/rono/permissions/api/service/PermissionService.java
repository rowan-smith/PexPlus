package dev.rono.permissions.api.service;

import dev.rono.permissions.api.query.PermissionQuery;

/**
 * Modern PermissionsEx integration API ({@code dev.rono.permissions.api}).
 *
 * <p>Registered on Spigot/Paper {@code ServicesManager} under this type.</p>
 *
 * <pre>{@code
 * pex.query().world(w).user(uuid).inGroup("vip", true);
 * pex.query().groups().count();
 * pex.query().backend().activate("file");
 * }</pre>
 */
public interface PermissionService {

    /**
     * Returns the fluent entry point for permission checks, subject edits, registry access,
     * backend administration, and maintenance operations.
     *
     * @return a {@link PermissionQuery} bound to this service instance
     */
    default PermissionQuery query() {
        return PermissionQuery.of(this);
    }

    /**
     * Casts {@code service} to {@link PermissionServiceBridge} when the runtime implementation
     * exposes the full bridge API.
     *
     * <p>The modern query layer requires {@link PermissionServiceBridge}; plugin code should
     * normally use {@link #query()} instead of calling this directly.</p>
     *
     * @param service the registered {@link PermissionService} instance
     * @return the same instance as a {@link PermissionServiceBridge}
     * @throws IllegalStateException if {@code service} does not implement {@link PermissionServiceBridge}
     */
    static PermissionServiceBridge requireBridge(PermissionService service) {
        if (service instanceof PermissionServiceBridge bridge) {
            return bridge;
        }
        throw new IllegalStateException(
                "PermissionService must implement PermissionServiceBridge: " + service.getClass().getName());
    }
}
