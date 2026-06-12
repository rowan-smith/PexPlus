package dev.rono.permissions.api.query;

import dev.rono.permissions.api.service.PexPermissionServiceBridge;
import dev.rono.permissions.api.session.PexPermissionEditSession;

/** Batch edit sessions — obtain via {@link dev.rono.permissions.api.service.PexPermissionService#session()}. */
public final class PexSessionScope {

    private final PexPermissionServiceBridge service;

    public PexSessionScope(PexPermissionServiceBridge service) {
        this.service = service;
    }

    public PexPermissionEditSession open() {
        return service.openEditSession();
    }

    /** Alias for {@link #open()}. */
    public PexPermissionEditSession start() {
        return open();
    }
}
