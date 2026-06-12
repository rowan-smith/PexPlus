package dev.rono.permissions.api.world;

/** Thrown when a world realm is not registered on the platform. */
public final class WorldNotFoundException extends RuntimeException {

    private final String name;

    public WorldNotFoundException(String name) {
        super("World not found: " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
