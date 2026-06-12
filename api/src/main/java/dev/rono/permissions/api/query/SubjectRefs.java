package dev.rono.permissions.api.query;

import dev.rono.permissions.api.service.PermissionServiceBridge;

/** Package-internal fluent scaffolding; use {@link PermissionService#query()}. */
final class SubjectRefs {

    private SubjectRefs() {}

    static UserRef user(PermissionServiceBridge service, java.util.UUID uuid, String identifier) {
        return new UserRef(service, uuid, identifier);
    }

    static GroupRef group(PermissionServiceBridge service, String name) {
        return new GroupRef(service, name);
    }
}
