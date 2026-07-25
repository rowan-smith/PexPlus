package dev.rono.permissions.api.permission;

import dev.rono.permissions.api.context.ContextKeys;
import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.options.OptionModifier;

import java.time.Duration;
import java.util.Objects;

public interface PermissionHolderModifier<Self extends PermissionHolderModifier<Self>> extends OptionModifier<Self> {

    /**
     * Adds or replaces an explicit permission node.
     *
     * <p>
     * A node is identified by its permission and exact context set.
     * If a matching node already exists, its value and expiry are replaced.
     * </p>
     *
     * @param node
     *            the node to set
     * @return this modifier
     */
    Self setPermission(PermissionNode node);

    default Self allowPermission(String permission) {
        return setPermission(PermissionNode.builder()
                .permission(permission)
                .build());
    }

    default Self allowPermission(String permission, ContextSet contexts) {
        Objects.requireNonNull(contexts, "contexts");

        return setPermission(PermissionNode.builder()
                .permission(permission)
                .contexts(contexts)
                .build());
    }

    default Self allowPermission(String permission, ContextKeys contextKey, String contextValue) {
        return allowPermission(permission, ContextSet.builder().add(contextKey, contextValue).build());
    }

    default Self denyPermission(String permission) {
        return setPermission(PermissionNode.builder()
                .permission(permission)
                .value(PermissionValue.DENY)
                .build());
    }

    default Self denyPermission(String permission, ContextSet contexts) {
        Objects.requireNonNull(contexts, "contexts");

        return setPermission(PermissionNode.builder()
                .permission(permission)
                .value(PermissionValue.DENY)
                .contexts(contexts)
                .build());
    }

    default Self denyPermission(String permission, ContextKeys contextKey, String contextValue) {
        return denyPermission(permission, ContextSet.builder().add(contextKey, contextValue).build());
    }

    default Self allowTimedPermission(String permission, Duration duration) {
        return setPermission(PermissionNode.builder()
                .permission(permission)
                .duration(duration)
                .build());
    }

    default Self allowTimedPermission(String permission, ContextSet contexts, Duration duration) {
        Objects.requireNonNull(contexts, "contexts");

        return setPermission(PermissionNode.builder()
                .permission(permission)
                .contexts(contexts)
                .duration(duration)
                .build());
    }

    default Self allowTimedPermission(String permission, ContextKeys contextKey, String contextValue, Duration duration) {
        return allowTimedPermission(permission, ContextSet.builder().add(contextKey, contextValue).build(), duration);
    }

    default Self denyTimedPermission(String permission, Duration duration) {
        return setPermission(PermissionNode.builder()
                .permission(permission)
                .value(PermissionValue.DENY)
                .duration(duration)
                .build());
    }

    default Self denyTimedPermission(String permission, ContextSet contexts, Duration duration) {
        Objects.requireNonNull(contexts, "contexts");

        return setPermission(PermissionNode.builder()
                .permission(permission)
                .value(PermissionValue.DENY)
                .contexts(contexts)
                .duration(duration)
                .build());
    }

    default Self denyTimedPermission(String permission, ContextKeys contextKey, String contextValue, Duration duration) {
        return denyTimedPermission(permission, ContextSet.builder().add(contextKey, contextValue).build(), duration);
    }

    /**
     * Removes the explicit permission assignment matching the permission and exact
     * context set.
     *
     * @param permission
     *            the permission identifier
     * @param contexts
     *            the exact contexts identifying the assignment
     * @return this modifier
     */
    Self removePermission(String permission, ContextSet contexts);

    default Self removePermission(String permission, ContextKeys contextKey, String contextValue) {
        return removePermission(permission, ContextSet.builder().add(contextKey, contextValue).build());
    }

    /** Removes all explicit permission assignments. */
    Self clearPermissions();

    /** Removes all explicit permission assignments with exactly these contexts. */
    Self clearPermissions(ContextSet contexts);

    default Self clearPermissions(ContextKeys contextKey, String contextValue) {
        return clearPermissions(ContextSet.builder().add(contextKey, contextValue).build());
    }

    default Self removePermission(String permission) {
        Objects.requireNonNull(permission, "permission");

        return removePermission(permission, ContextSet.empty());
    }

    default Self removePermission(PermissionNode node) {
        Objects.requireNonNull(node, "node");

        return removePermission(node.permission(), node.contexts());
    }
}
