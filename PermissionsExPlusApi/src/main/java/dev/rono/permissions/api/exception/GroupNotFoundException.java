package dev.rono.permissions.api.exception;

@SuppressWarnings("serial")
public final class GroupNotFoundException extends PermissionsException {
    public GroupNotFoundException(String name) {
        super("Group not found: " + name);
    }
}
