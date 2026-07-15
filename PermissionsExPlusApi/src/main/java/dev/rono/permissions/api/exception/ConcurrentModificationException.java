package dev.rono.permissions.api.exception;

@SuppressWarnings("serial")
public final class ConcurrentModificationException extends PermissionsException {
    public ConcurrentModificationException(String message) {
        super(message);
    }
}
