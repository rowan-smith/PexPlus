package dev.rono.permissions.core;

import dev.rono.permissions.api.PexProvider;

public final class PexImplProvider {

    private PexImplProvider() {
        throw new AssertionError();
    }

    public static PexApiImpl<?> get() {
        if (!PexProvider.available()) {
            throw new IllegalStateException("PermissionsExPlus has not been initialized");
        }

        return (PexApiImpl<?>) PexProvider.get();
    }
}
