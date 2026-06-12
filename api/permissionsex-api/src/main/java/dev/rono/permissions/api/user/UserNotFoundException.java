package dev.rono.permissions.api.user;

import java.util.UUID;

/** Thrown when a persisted user record does not exist. */
public final class UserNotFoundException extends RuntimeException {

    private final String identifier;

    public UserNotFoundException(UUID uuid) {
        super("User not found: " + uuid);
        this.identifier = uuid.toString();
    }

    public UserNotFoundException(String name) {
        super("User not found: " + name);
        this.identifier = name;
    }

    public String getIdentifier() {
        return identifier;
    }
}
