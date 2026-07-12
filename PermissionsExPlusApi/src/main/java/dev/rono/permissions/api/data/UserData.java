package dev.rono.permissions.api.data;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface UserData {

    UUID id();

    String username();

    Set<String> groups();

    Set<PermissionEntry> permissions();

    Map<String, String> options();

    record PermissionEntry(
            String permission,
            String context,
            int expirySeconds
    ) {}

}
