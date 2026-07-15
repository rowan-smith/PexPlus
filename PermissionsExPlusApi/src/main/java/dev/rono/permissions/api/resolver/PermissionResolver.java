package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionResult;

/**
 * Resolves effective permissions, including inherited and default groups.
 *
 * <p>
 * Expired or context-inapplicable nodes are ignored. Candidates are ordered
 * by exact permission before the longest matching wildcard, greater context
 * specificity, shorter inheritance distance, then greater group weight. An
 * explicit deny wins when all preceding precedence dimensions tie. Cycles are
 * visited at most once and cannot make resolution recurse indefinitely.
 * Inherited and default candidates are excluded when their corresponding
 * {@link QueryOptions} flags are disabled.
 * </p>
 */
public interface PermissionResolver {

    PermissionResult check(PermissionHolder holder, String permission, QueryOptions options);

    PermissionResolution explain(PermissionHolder holder, String permission, QueryOptions options);

    default PermissionResult check(PermissionHolder holder, String permission, ContextSet contexts) {
        return check(holder, permission, QueryOptions.builder().contexts(contexts).build());
    }

    default PermissionResolution explain(PermissionHolder holder, String permission, ContextSet contexts) {
        return explain(holder, permission, QueryOptions.builder().contexts(contexts).build());
    }

    default boolean hasPermission(PermissionHolder holder, String permission, QueryOptions options) {
        return check(holder, permission, options) == PermissionResult.ALLOW;
    }

    default boolean hasPermission(PermissionHolder holder, String permission, ContextSet contexts) {
        return check(holder, permission, contexts) == PermissionResult.ALLOW;
    }
}
