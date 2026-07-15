package dev.rono.permissions.api.permission;

final class PermissionNodes {

    private PermissionNodes() {
        throw new AssertionError();
    }

    static PermissionNodeBuilder builder() {
        return new PermissionNodeBuilderImpl();
    }
}
