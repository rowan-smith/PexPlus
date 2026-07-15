package dev.rono.permissions.api.exception;

@SuppressWarnings("serial")
public final class LadderAlreadyExistsException extends PermissionsException {
    public LadderAlreadyExistsException(String name) {
        super("Ladder already exists: " + name);
    }
}
