package dev.rono.permissions.core.model;

import dev.rono.permissions.api.options.OptionNode;
import dev.rono.permissions.api.parent.ParentNode;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.user.User;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record UserSnapshot(
        UUID uniqueId,
        String name,
        Set<PermissionNode> explicitPermissions,
        Set<OptionNode> explicitOptions,
        Set<ParentNode> groups) implements User {

    public UserSnapshot {
        Objects.requireNonNull(uniqueId, "uniqueId");

        name = Objects.requireNonNull(name, "name").trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be blank");
        }

        explicitPermissions = Set.copyOf(explicitPermissions);
        explicitOptions = Set.copyOf(explicitOptions);

        groups = Set.copyOf(groups);
    }
}
