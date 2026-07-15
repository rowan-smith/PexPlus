package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.user.User;

import java.util.Set;

/**
 * Resolves transitive, context-applicable group inheritance without looping on
 * cycles.
 */
public interface InheritanceResolver {

    /**
     * Returns applicable direct and transitive groups when inheritance is
     * enabled, or applicable directly assigned groups only when disabled.
     * The configured implicit default is included only when enabled and the
     * user's data record contains no assigned groups.
     */
    Set<Group> groups(User user, QueryOptions options);

    /**
     * Returns applicable transitive parents when inheritance is enabled, or
     * applicable direct parents only when disabled.
     */
    Set<Group> parents(Group group, QueryOptions options);

    /** Uses the same direct/transitive and default rules as {@link #groups}. */
    boolean inherits(User user, String group, QueryOptions options);

    /** Uses the same direct/transitive rules as {@link #parents}. */
    boolean inherits(Group group, String parent, QueryOptions options);

    default Set<Group> groups(User user, ContextSet contexts) {
        return groups(user, QueryOptions.builder().contexts(contexts).build());
    }

    default Set<Group> parents(Group group, ContextSet contexts) {
        return parents(group, QueryOptions.builder().contexts(contexts).build());
    }

    default boolean inherits(User user, String group, ContextSet contexts) {
        return inherits(user, group, QueryOptions.builder().contexts(contexts).build());
    }

    default boolean inherits(Group group, String parent, ContextSet contexts) {
        return inherits(group, parent, QueryOptions.builder().contexts(contexts).build());
    }
}
