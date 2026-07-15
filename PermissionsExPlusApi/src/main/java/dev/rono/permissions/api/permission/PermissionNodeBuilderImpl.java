package dev.rono.permissions.api.permission;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.util.Identifiers;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

final class PermissionNodeBuilderImpl implements PermissionNodeBuilder {

    private String permission;

    private PermissionValue value = PermissionValue.ALLOW;

    private ContextSet contexts = ContextSet.empty();

    private Instant expiry;

    @Override
    public PermissionNodeBuilder permission(String permission) {
        this.permission = Identifiers.permission(permission);

        return this;
    }

    @Override
    public PermissionNodeBuilder value(PermissionValue value) {
        this.value = Objects.requireNonNull(value, "value");

        return this;
    }

    @Override
    public PermissionNodeBuilder contexts(ContextSet contexts) {
        this.contexts = Objects.requireNonNull(contexts, "contexts");

        return this;
    }

    @Override
    public PermissionNodeBuilder expiry(Instant expiry) {
        this.expiry = Objects.requireNonNull(expiry, "expiry");

        return this;
    }

    @Override
    public PermissionNodeBuilder permanent() {
        this.expiry = null;

        return this;
    }

    @Override
    public PermissionNode build() {
        if (permission == null) {
            throw new IllegalStateException("permission must be provided");
        }

        return new PermissionNodeImpl(permission, value, contexts, Optional.ofNullable(expiry));
    }
}
