package dev.rono.permissions.api;

public final class PermissionsExPlusProvider {
    private static PermissionsExPlusApi api;

    public static PermissionsExPlusApi get() {
        if (api == null) {
            throw new IllegalStateException("PermissionsExPlus has not been initialized");
        }

        return api;
    }

    public static boolean available()
    {
        return api != null;
    }

    public static void set(PermissionsExPlusApi api)
    {
        if (PermissionsExPlusProvider.api != null) {
            throw new IllegalStateException("PermissionsExPlus API already initialized");
        }

        PermissionsExPlusProvider.api = api;
    }
}