package dev.rono.permissions.api.platform.context;

/** Contributes active contexts for a platform-specific subject. */
@FunctionalInterface
public interface ContextCalculator<T> {

    void calculate(T subject, ContextConsumer consumer);
}
