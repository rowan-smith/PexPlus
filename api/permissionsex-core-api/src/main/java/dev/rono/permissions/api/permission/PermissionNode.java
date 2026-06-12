package dev.rono.permissions.api.permission;

import java.time.Instant;
import java.util.Map;

/** Result of adding a permission node to a holder. */
public interface PermissionNode {

    PermissionHolder holder();

    String permission();

    Instant expiresAt();

    Map<String, String> context();

    PermissionSource source();
}
