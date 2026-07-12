package dev.rono.permissions.api.group;

public class GroupAlreadyExistsException extends RuntimeException {
    private final String name;

    /**
     * Creates an exception for a duplicate group.
     *
     * @param name group identifier that already exists
     */
    public GroupAlreadyExistsException(String name) {
        super("Group already exists: " + name);
        this.name = name;
    }

    /**
     * Returns the conflicting group name.
     *
     * @return existing group identifier
     */
    public String getName() {
        return name;
    }
}