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

    /** Entry point for checks, edits, registry, backend, and maintenance. */
    default PermissionQuery query() {
        return PermissionQuery.of(this);
    }

    /** @throws IllegalStateException if the runtime does not expose {@link PermissionServiceBridge} */
    static PermissionServiceBridge requireBridge(PermissionService service) {
        if (service instanceof PermissionServiceBridge bridge) {
            return bridge;
        }
        throw new IllegalStateException(
                "PermissionService must implement PermissionServiceBridge: " + service.getClass().getName());
    }
}
