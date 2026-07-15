package dev.rono.permissions.api.exception;

@SuppressWarnings("serial")
public final class LadderNotFoundException extends PermissionsException {
    public LadderNotFoundException(String name) {
        super("Ladder not found: " + name);
    }
}
