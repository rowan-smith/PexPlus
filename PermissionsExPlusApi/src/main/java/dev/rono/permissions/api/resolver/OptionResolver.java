package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.options.OptionKeys;
import dev.rono.permissions.api.permission.PermissionHolder;

import java.util.Optional;

/**
 * Resolves effective options through contextual group inheritance. Expired and
 * inapplicable nodes are ignored; context specificity, inheritance distance,
 * and group weight determine precedence in that order. Prefix and suffix use
 * the same single-value semantics and are not priority-stacked. Inherited and
 * default options are excluded when their corresponding query flags are
 * disabled.
 */
public interface OptionResolver {

    Optional<String> resolve(PermissionHolder holder, String key, QueryOptions options);

    default Optional<String> resolve(PermissionHolder holder, String key, ContextSet contexts) {
        return resolve(holder, key, QueryOptions.builder().contexts(contexts).build());
    }

    default Optional<String> prefix(PermissionHolder holder, QueryOptions options) {
        return resolve(holder, OptionKeys.PREFIX, options);
    }

    default Optional<String> prefix(PermissionHolder holder, ContextSet contexts) {
        return resolve(holder, OptionKeys.PREFIX, contexts);
    }

    default Optional<String> suffix(PermissionHolder holder, QueryOptions options) {
        return resolve(holder, OptionKeys.SUFFIX, options);
    }

    default Optional<String> suffix(PermissionHolder holder, ContextSet contexts) {
        return resolve(holder, OptionKeys.SUFFIX, contexts);
    }
}
