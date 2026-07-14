package dev.rono.permissions.api.permission;

import dev.rono.permissions.api.metadata.MetadataMap;
import dev.rono.permissions.api.realm.Realm;

import java.time.Instant;
import java.util.Optional;

public interface PermissionNode {
    String permission();

    PermissionValue value();

    Realm realm();

    int priority();

    Optional<Instant> expires();

    MetadataMap metadata();
}