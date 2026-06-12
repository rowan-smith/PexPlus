package dev.rono.permissions.api.group;

/** Thrown when {@link GroupManager#createGroup} targets a name that already exists. */
public final class GroupAlreadyExistsException extends RuntimeException {

    private final String name;

    public GroupAlreadyExistsException(String name) {
        super("Group already exists: " + name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
