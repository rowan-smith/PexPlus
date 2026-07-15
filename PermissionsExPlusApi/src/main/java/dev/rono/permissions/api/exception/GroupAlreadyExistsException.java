package dev.rono.permissions.api.exception;

@SuppressWarnings("serial")
public final class GroupAlreadyExistsException extends PermissionsException {
    public GroupAlreadyExistsException(String name) {
        super("Group already exists: " + name);
    }
}
