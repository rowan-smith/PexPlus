package dev.rono.permissions.api.exception;

@SuppressWarnings("serial")
public final class UserNotFoundException extends PermissionsException {
    public UserNotFoundException(String identifier) {
        super("User not found: " + identifier);
    }
}
