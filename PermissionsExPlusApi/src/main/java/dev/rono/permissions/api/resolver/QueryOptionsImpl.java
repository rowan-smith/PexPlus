package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.context.ContextSet;

import java.util.Objects;

record QueryOptionsImpl(
        ContextSet contexts,
        boolean includeInheritance,
        boolean includeDefaults) implements QueryOptions {

    QueryOptionsImpl {
        Objects.requireNonNull(contexts, "contexts");
    }
}
