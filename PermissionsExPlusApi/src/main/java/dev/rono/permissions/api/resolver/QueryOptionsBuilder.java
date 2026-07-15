package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.context.ContextSet;

public interface QueryOptionsBuilder {

    QueryOptionsBuilder contexts(ContextSet contexts);

    QueryOptionsBuilder includeInheritance(boolean includeInheritance);

    QueryOptionsBuilder includeDefaults(boolean includeDefaults);

    QueryOptions build();
}
