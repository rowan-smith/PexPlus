package dev.rono.permissions.api.platform.context;

import dev.rono.permissions.api.context.ContextSet;
import dev.rono.permissions.api.context.ContextRegistry;
import dev.rono.permissions.api.resolver.QueryOptions;

/** Combines cached platform state with independent third-party calculators. */
public interface ContextManager<T> {

    /**
     * Registers a read-only context calculator.
     *
     * <p>
     * Calculators may run asynchronously and should read thread-safe plugin-owned
     * state rather than call thread-confined platform APIs.
     * </p>
     *
     * @param calculator
     *            context contributor
     * @return removable lifecycle registration
     */
    ContextRegistration registerCalculator(ContextCalculator<? super T> calculator);

    ContextSet contexts(T subject);

    QueryOptions queryOptions(T subject);

    ContextSet staticContexts();

    /**
     * Returns the command validation and completion registry.
     *
     * @return public context registry
     */
    ContextRegistry registry();
}
