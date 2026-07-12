package dev.rono.permissions.api.group;

public class GroupNotFoundException extends RuntimeException {
    private final String name;

    /**
     * Creates an exception for a missing group.
     *
     * @param name group identifier that was not found
     */
    public GroupNotFoundException(String name) {
        super("Group not found: " + name);
        this.name = name;
    }

    /**
     * Returns the group name that was looked up.
     *
     * @return missing group identifier
     */
    public String getName() {
        return name;
    }}
