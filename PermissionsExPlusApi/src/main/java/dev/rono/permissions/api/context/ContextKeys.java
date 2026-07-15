package dev.rono.permissions.api.context;

public final class ContextKeys {
    public static final String WORLD = "world";

    public static final String SERVER = "server";

    public static final String DIMENSION = "dimension";

    public static final String GAMEMODE = "gamemode";

    public static final String PROXY = "proxy";

    public static final String REALM = "realm";

    private ContextKeys() {
        throw new AssertionError();
    }
}
