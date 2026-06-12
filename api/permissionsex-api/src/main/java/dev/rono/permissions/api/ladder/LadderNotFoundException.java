package dev.rono.permissions.api.ladder;

/** Thrown when a rank ladder is not defined. */
public final class LadderNotFoundException extends RuntimeException {

    private final String name;

    public LadderNotFoundException(String name) {
        super("Ladder not found: " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
