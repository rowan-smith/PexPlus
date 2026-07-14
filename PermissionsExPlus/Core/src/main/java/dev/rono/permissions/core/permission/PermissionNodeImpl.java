package dev.rono.permissions.core.permission;

import dev.rono.permissions.api.metadata.MetadataMap;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.permission.PermissionValue;
import dev.rono.permissions.api.realm.BuiltinRealm;
import dev.rono.permissions.api.realm.Realm;
import dev.rono.permissions.core.metadata.MetadataMapImpl;

import java.time.Instant;
import java.util.Optional;

public final class PermissionNodeImpl implements PermissionNode {

    private final String permission;
    private final PermissionValue value;
    private final Realm realm;
    private final int priority;
    private final Optional<Instant> expires;
    private final MetadataMap metadata;

    public PermissionNodeImpl(String permission) {
        this(permission, PermissionValue.TRUE, BuiltinRealm.GLOBAL, 0, Optional.empty(), new MetadataMapImpl());
    }

    public PermissionNodeImpl(
        String permission,
        PermissionValue value,
        Realm realm,
        int priority,
        Optional<Instant> expires,
        MetadataMap metadata
    ) {
        this.permission = permission;
        this.value = value;
        this.realm = realm;
        this.priority = priority;
        this.expires = expires;
        this.metadata = metadata;
    }

    @Override
    public String permission() {
        return permission;
    }

    @Override
    public PermissionValue value() {
        return value;
    }

    @Override
    public Realm realm() {
        return realm;
    }

    @Override
    public int priority() {
        return priority;
    }

    @Override
    public Optional<Instant> expires() {
        return expires;
    }

    @Override
    public MetadataMap metadata() {
        return metadata;
    }
}
