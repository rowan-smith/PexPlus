package dev.rono.permissions.api.context;

public enum ContextKeys {
    WORLD("world"),
    SERVER("server"),
    GAMEMODE("gamemode"),
    PROXY("proxy");

    private final String key;

    ContextKeys(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
