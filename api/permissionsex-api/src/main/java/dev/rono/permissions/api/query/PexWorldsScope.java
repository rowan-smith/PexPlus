package dev.rono.permissions.api.query;

import dev.rono.permissions.api.service.PexPermissionServiceBridge;
import java.util.Collection;

/** Registered server realms — obtain via {@link dev.rono.permissions.api.service.PexPermissionService#worlds()}. */
public final class PexWorldsScope {

    private final PexPermissionServiceBridge service;

    public PexWorldsScope(PexPermissionServiceBridge service) {
        this.service = service;
    }

    public int count() {
        return service.registeredWorlds().size();
    }

    public Collection<String> names() {
        return service.registeredWorlds();
    }
}
