package dev.rono.permissions.api.context;

import java.util.Map;

final class ContextSets {

    private static final ContextSet EMPTY = new ImmutableContextSet(Map.of());

    private ContextSets() {
        throw new AssertionError();
    }

    static ContextSet empty() {
        return EMPTY;
    }

    static ContextBuilder builder() {
        return new ContextBuilderImpl();
    }
}
