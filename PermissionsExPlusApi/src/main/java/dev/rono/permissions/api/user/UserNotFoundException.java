package dev.rono.permissions.api.user;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    private final String identifier;

    /**
     * Creates an exception for a missing UUID-keyed user.
     *
     * @param uuid user UUID that was not found
     */
    public UserNotFoundException(UUID uuid) {
        super("User not found: " + uuid);
        this.identifier = uuid.toString();
    }

    /**
     * Creates an exception for a missing name-keyed user.
     *
     * @param name user name that was not found
     */
    public UserNotFoundException(String name) {
        super("User not found: " + name);
        this.identifier = name;
    }

    /**
     * Returns the identifier that was looked up.
     *
     * @return UUID string or name depending on which constructor was used
     */
    public String getIdentifier() {
        return identifier;
    }}
