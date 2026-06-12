package dev.rono.permissions.api.ladder;

/** Thrown when {@link LadderManager#createLadder} targets a ladder that already exists. */
public final class LadderAlreadyExistsException extends RuntimeException {

    private final String name;

    public LadderAlreadyExistsException(String name) {
        super("Ladder already exists: " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
