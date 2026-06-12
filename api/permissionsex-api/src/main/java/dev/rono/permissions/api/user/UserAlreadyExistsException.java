package dev.rono.permissions.api.user;

import java.util.UUID;

/** Thrown when {@link UserManager#createUser} targets an identifier that already exists. */
public final class UserAlreadyExistsException extends RuntimeException {

    private final String identifier;

    public UserAlreadyExistsException(UUID uuid) {
        super("User already exists: " + uuid);
        this.identifier = uuid.toString();
    }

    public UserAlreadyExistsException(String name) {
        super("User already exists: " + name);
        this.identifier = name;
    }

    public String getIdentifier() {
        return identifier;
    }
}
