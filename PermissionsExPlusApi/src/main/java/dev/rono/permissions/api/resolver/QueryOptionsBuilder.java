package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.context.ContextKeys;
import dev.rono.permissions.api.context.ContextSet;

public interface QueryOptionsBuilder {

    QueryOptionsBuilder contexts(ContextSet contexts);

    default QueryOptionsBuilder contexts(ContextKeys key, String value) {
        return contexts(ContextSet.builder().add(key, value).build());
    }

    QueryOptionsBuilder includeInheritance(boolean includeInheritance);

    QueryOptionsBuilder includeDefaults(boolean includeDefaults);

    QueryOptions build();
}
