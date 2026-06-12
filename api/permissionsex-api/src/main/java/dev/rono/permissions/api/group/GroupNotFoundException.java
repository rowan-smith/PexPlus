package dev.rono.permissions.api.group;

/** Thrown when a persisted group record does not exist. */
public final class GroupNotFoundException extends RuntimeException {

    private final String name;

    public GroupNotFoundException(String name) {
        super("Group not found: " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
