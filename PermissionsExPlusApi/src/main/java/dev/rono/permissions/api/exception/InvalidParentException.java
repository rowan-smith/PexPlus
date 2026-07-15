package dev.rono.permissions.api.exception;

@SuppressWarnings("serial")
public final class InvalidParentException extends PermissionsException {
    public InvalidParentException(String message) {
        super(message);
    }
}
