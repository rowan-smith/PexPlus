package dev.rono.permissions.api.data;

import java.util.Map;
import java.util.Set;

public interface GroupData {

    String name();

    Set<String> parents();

    Set<PermissionEntry> permissions();

    Map<String, String> options();

    record PermissionEntry(
            String permission,
            String context,
            int expirySeconds
    ) {}

}
