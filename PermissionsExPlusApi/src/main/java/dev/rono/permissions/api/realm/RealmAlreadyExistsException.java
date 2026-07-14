package dev.rono.permissions.api.realm;

public class RealmAlreadyExistsException extends RuntimeException {
    public RealmAlreadyExistsException(String message) {
        super(message);
    }
}
