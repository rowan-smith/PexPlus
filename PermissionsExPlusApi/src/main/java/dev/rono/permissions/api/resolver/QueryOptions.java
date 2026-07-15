package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.context.ContextSet;

import java.util.Objects;

/** Context and policy inputs used to calculate effective permission data. */
public interface QueryOptions {

    ContextSet contexts();

    /**
     * Whether transitive inherited groups participate. When false, user group
     * queries return only applicable direct memberships and group parent queries
     * return only applicable direct parents.
     */
    boolean includeInheritance();

    /**
     * Whether the configured implicit default group participates when a user's
     * data record contains no assigned groups. This flag does not affect an
     * explicit
     * call to {@link DefaultGroupResolver}.
     */
    boolean includeDefaults();

    static QueryOptions global() {
        return builder().build();
    }

    static QueryOptionsBuilder builder() {
        return new QueryOptionsBuilderImpl();
    }

    static QueryOptionsBuilder builder(QueryOptions options) {
        Objects.requireNonNull(options, "options");

        return builder()
                .contexts(options.contexts())
                .includeInheritance(options.includeInheritance())
                .includeDefaults(options.includeDefaults());
    }
}
