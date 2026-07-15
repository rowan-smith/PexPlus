package dev.rono.permissions.api.exception;

/** Base type for failures exposed by asynchronous API operations. */
@SuppressWarnings("serial")
public class PermissionsException extends RuntimeException {

    public PermissionsException(String message) {
        super(message);
    }

    public PermissionsException(String message, Throwable cause) {
        super(message, cause);
    }
}
