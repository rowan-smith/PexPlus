package dev.rono.permissions.core.model;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.options.OptionNode;
import dev.rono.permissions.api.parent.ParentNode;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.util.Identifiers;

import java.util.OptionalInt;
import java.util.Set;

public record GroupSnapshot(String name, OptionalInt weight, Set<PermissionNode> explicitPermissions, Set<OptionNode> explicitOptions, Set<ParentNode> parents) implements Group {
    public GroupSnapshot {
        name = Identifiers.group(name);
        weight = weight == null ? OptionalInt.empty() : weight;
        explicitPermissions = Set.copyOf(explicitPermissions);
        explicitOptions = Set.copyOf(explicitOptions);
        parents = Set.copyOf(parents);
    }
}
