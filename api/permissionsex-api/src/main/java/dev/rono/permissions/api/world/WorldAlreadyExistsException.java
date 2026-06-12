package dev.rono.permissions.api.world;

/** Thrown when {@link WorldManager#createWorld} targets a realm that already exists. */
public final class WorldAlreadyExistsException extends RuntimeException {

    private final String name;

    public WorldAlreadyExistsException(String name) {
        super("World already exists: " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
