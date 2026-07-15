package dev.rono.permissions.api.exception;

@SuppressWarnings("serial")
public final class StorageException extends PermissionsException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
