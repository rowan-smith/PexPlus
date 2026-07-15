package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.context.ContextSet;

import java.util.Objects;

final class QueryOptionsBuilderImpl implements QueryOptionsBuilder {

    private ContextSet contexts = ContextSet.empty();

    private boolean includeInheritance = true;

    private boolean includeDefaults = true;

    @Override
    public QueryOptionsBuilder contexts(ContextSet contexts) {
        this.contexts = Objects.requireNonNull(contexts, "contexts");

        return this;
    }

    @Override
    public QueryOptionsBuilder includeInheritance(boolean includeInheritance) {
        this.includeInheritance = includeInheritance;

        return this;
    }

    @Override
    public QueryOptionsBuilder includeDefaults(boolean includeDefaults) {
        this.includeDefaults = includeDefaults;

        return this;
    }

    @Override
    public QueryOptions build() {
        return new QueryOptionsImpl(contexts, includeInheritance, includeDefaults);
    }
}
