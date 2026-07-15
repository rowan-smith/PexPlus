package dev.rono.permissions.api;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class PexRegistration {

    private PexRegistration() {
        throw new AssertionError();
    }

    @Internal
    public static void register(PexApi api) {
        PexProvider.register(api);
    }

    @Internal
    public static void unregister(PexApi api) {
        PexProvider.unregister(api);
    }
}
