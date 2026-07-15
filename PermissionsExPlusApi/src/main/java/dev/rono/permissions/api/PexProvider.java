package dev.rono.permissions.api;

import java.util.Objects;

/**
 * Provides a central mechanism for accessing the PermissionsExPlus API
 * instance.
 * This class ensures that the PermissionsExPlus API is initialized and
 * available
 * for usage, while also managing its registration and unregistration.
 * <p>
 * This class is designed as a utility and cannot be instantiated.
 */
public final class PexProvider {
    private static volatile PexApi api;

    private PexProvider() {
        throw new AssertionError();
    }

    /**
     * Retrieves the currently initialized instance of the PermissionsExPlus API.
     * This method ensures that the API instance is available and has been properly
     * initialized
     * before it is accessed. If the API has not been initialized, an
     * {@code IllegalStateException}
     * will be thrown.
     *
     * @return the active instance of {@code PexApi}
     * @throws IllegalStateException
     *             if the PermissionsExPlus API has not been initialized
     */
    public static PexApi get() {
        PexApi current = api;

        if (current == null) {
            throw new IllegalStateException("PermissionsExPlus has not been initialized");
        }

        return current;
    }

    /**
     * Checks whether the PermissionsExPlus API is currently available.
     * <p>
     * This method determines if the API instance has been initialized and
     * is ready to be accessed. If the API has not been registered or has
     * been unregistered, this method will return {@code false}.
     *
     * @return {@code true} if the PermissionsExPlus API is initialized
     *         and available; {@code false} otherwise
     */
    public static boolean available() {
        return api != null;
    }

    /**
     * Registers the provided {@code PexApi} instance as the central API instance
     * for the application.
     * This method ensures that the API can only be registered once and throws an
     * exception
     * if an attempt is made to re-register a different instance.
     *
     * @param apiInstance
     *            the {@code PexApi} instance to be registered
     * @throws IllegalStateException
     *             if an API instance has already been registered
     *             and it differs from the provided instance
     */
    static synchronized void register(PexApi apiInstance) {
        Objects.requireNonNull(apiInstance, "apiInstance");

        if (PexProvider.api != null && PexProvider.api != apiInstance) {
            throw new IllegalStateException("PermissionsExPlus API already initialized");
        }

        PexProvider.api = apiInstance;
    }

    /**
     * Unregisters the currently registered {@code PexApi} instance if it matches
     * the provided {@code PexApi} instance.
     * <p>
     * This method allows for safely removing the current API instance from the
     * {@code PexProvider} if it has already been registered. If the provided
     * {@code apiInstance} does not match the currently registered instance, no
     * action
     * will be performed.
     *
     * @param apiInstance
     *            the {@code PexApi} instance to be unregistered; should match
     *            the instance currently registered in {@code PexProvider}
     */
    static synchronized void unregister(PexApi apiInstance) {
        Objects.requireNonNull(apiInstance, "apiInstance");

        if (PexProvider.api == apiInstance) {
            PexProvider.api = null;
        }
    }
}
