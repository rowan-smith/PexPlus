package dev.rono.permissions.api.group;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.parent.ParentNode;
import dev.rono.permissions.api.permission.PermissionHolderModifier;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;

public interface GroupModifier extends PermissionHolderModifier<GroupModifier> {

    GroupModifier setWeight(int weight);

    GroupModifier clearWeight();

    /**
     * Adds a directly inherited parent group.
     *
     * <p>
     * The modification fails if the parent does not exist, the group attempts
     * to inherit itself, or the relationship would create an inheritance
     * cycle.
     * </p>
     *
     * @param node
     *            the parent node
     * @return this modifier
     */
    GroupModifier addParent(ParentNode node);

    default GroupModifier addParent(String group) {
        return addParent(ParentNode.builder().group(group).build());
    }

    default GroupModifier addParent(String group, ContextSet contexts) {
        Objects.requireNonNull(contexts, "contexts");

        return addParent(ParentNode.builder().group(group).contexts(contexts).build());
    }

    default GroupModifier addTemporaryParent(String group, Duration duration) {
        return addParent(ParentNode.builder().group(group).duration(duration).build());
    }

    default GroupModifier addTemporaryParent(String group, ContextSet contexts, Duration duration) {
        Objects.requireNonNull(contexts, "contexts");

        return addParent(ParentNode.builder()
                .group(group)
                .contexts(contexts)
                .duration(duration)
                .build());
    }

    GroupModifier removeParent(String group, ContextSet contexts);

    default GroupModifier removeParent(String group) {
        return removeParent(group, ContextSet.empty());
    }

    default GroupModifier removeParent(ParentNode node) {
        Objects.requireNonNull(node, "node");

        return removeParent(node.group(), node.contexts());
    }

    GroupModifier clearParents();

    GroupModifier setParents(Collection<ParentNode> parents);
}
