package dev.rono.permissions.api.user;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.parent.ParentNode;
import dev.rono.permissions.api.permission.PermissionHolderModifier;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;

public interface UserModifier extends PermissionHolderModifier<UserModifier> {

    UserModifier updateName(String username);

    UserModifier addGroup(ParentNode node);

    default UserModifier addGroup(String group) {
        return addGroup(ParentNode.builder().group(group).build());
    }

    default UserModifier addGroup(String group, ContextSet contexts) {
        Objects.requireNonNull(contexts, "contexts");

        return addGroup(ParentNode.builder().group(group).contexts(contexts).build());
    }

    default UserModifier addTemporaryGroup(String group, Duration duration) {
        return addGroup(ParentNode.builder().group(group).duration(duration).build());
    }

    default UserModifier addTemporaryGroup(String group, ContextSet contexts, Duration duration) {
        Objects.requireNonNull(contexts, "contexts");

        return addGroup(ParentNode.builder()
                .group(group)
                .contexts(contexts)
                .duration(duration)
                .build());
    }

    UserModifier removeGroup(String group, ContextSet contexts);

    default UserModifier removeGroup(String group) {
        return removeGroup(group, ContextSet.empty());
    }

    default UserModifier removeGroup(ParentNode node) {
        Objects.requireNonNull(node, "node");

        return removeGroup(node.group(), node.contexts());
    }

    UserModifier clearGroups();

    UserModifier setGroups(Collection<ParentNode> groups);
}
