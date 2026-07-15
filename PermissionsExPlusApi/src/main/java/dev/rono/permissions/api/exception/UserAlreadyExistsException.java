package dev.rono.permissions.api.exception;

@SuppressWarnings("serial")
public final class UserAlreadyExistsException extends PermissionsException {
    public UserAlreadyExistsException(String identifier) {
        super("User already exists: " + identifier);
    }
}
