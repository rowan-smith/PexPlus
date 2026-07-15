package dev.rono.permissions.api.parent;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.util.Identifiers;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

final class ParentNodeBuilderImpl implements ParentNodeBuilder {

    private String group;

    private ContextSet contexts = ContextSet.empty();

    private Instant expiry;

    @Override
    public ParentNodeBuilder group(String group) {
        this.group = Identifiers.group(group);

        return this;
    }

    @Override
    public ParentNodeBuilder contexts(ContextSet contexts) {
        this.contexts = Objects.requireNonNull(contexts, "contexts");

        return this;
    }

    @Override
    public ParentNodeBuilder expiry(Instant expiry) {
        this.expiry = Objects.requireNonNull(expiry, "expiry");

        return this;
    }

    @Override
    public ParentNodeBuilder permanent() {
        this.expiry = null;

        return this;
    }

    @Override
    public ParentNode build() {
        if (group == null) {
            throw new IllegalStateException("group must be provided");
        }

        return new ParentNodeImpl(group, contexts, Optional.ofNullable(expiry));
    }
}
