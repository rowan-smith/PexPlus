package dev.rono.permissions.api.query;

import dev.rono.permissions.api.service.PexPermissionServiceBridge;

/** Package-internal fluent scaffolding; use {@link dev.rono.permissions.api.service.PexPermissionService} entry points. */
final class SubjectRefs {

    private SubjectRefs() {}

    static UserRef user(PexPermissionServiceBridge service, java.util.UUID uuid, String identifier) {
        return new UserRef(service, uuid, identifier);
    }

    static GroupRef group(PexPermissionServiceBridge service, String name) {
        return new GroupRef(service, name);
    }
}
