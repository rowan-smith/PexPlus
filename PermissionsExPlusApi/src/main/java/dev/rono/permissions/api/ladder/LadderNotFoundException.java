package dev.rono.permissions.api.ladder;

public class LadderNotFoundException extends RuntimeException {
    public LadderNotFoundException(String message) {
        super(message);
    }
}
