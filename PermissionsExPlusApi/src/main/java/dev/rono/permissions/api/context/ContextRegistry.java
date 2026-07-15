package dev.rono.permissions.api.context;

import dev.rono.permissions.api.platform.context.ContextRegistration;

import java.util.Collection;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Registry of context keys and their live command values.
 */
public interface ContextRegistry {

    /**
     * Registers or replaces a context type.
     *
     * @param key
     *            context key, without the leading {@code --}
     * @param valueSupplier
     *            supplier queried for validation and tab completion
     * @return removable lifecycle registration
     */
    ContextRegistration registerContextType(String key, Supplier<Collection<String>> valueSupplier);

    /**
     * Returns all currently registered context keys.
     *
     * @return immutable context-key set
     */
    Set<String> registeredKeys();

    /**
     * Returns the current valid values for a context key.
     *
     * @param key
     *            context key
     * @return immutable value collection, or an empty collection for an unknown key
     */
    Collection<String> validValues(String key);
}
