package dev.rono.permissions.core;

import dev.rono.permissions.api.PexProvider;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class PexImplProvider {

    private PexImplProvider() {
        throw new AssertionError();
    }

    @Internal
    public static PexApiImpl<?> get() {
        if (!PexProvider.available()) {
            throw new IllegalStateException("PermissionsExPlus has not been initialized");
        }

        return (PexApiImpl<?>) PexProvider.get();
    }
}
