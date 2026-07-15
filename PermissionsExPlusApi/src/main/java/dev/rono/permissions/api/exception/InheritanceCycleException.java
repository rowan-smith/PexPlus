package dev.rono.permissions.api.exception;

@SuppressWarnings("serial")
public final class InheritanceCycleException extends PermissionsException {
    public InheritanceCycleException(String message) {
        super(message);
    }
}
