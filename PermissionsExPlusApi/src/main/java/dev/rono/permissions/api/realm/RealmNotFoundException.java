package dev.rono.permissions.api.realm;

public class RealmNotFoundException extends RuntimeException {
    public RealmNotFoundException(String message) {
        super(message);
    }
}
