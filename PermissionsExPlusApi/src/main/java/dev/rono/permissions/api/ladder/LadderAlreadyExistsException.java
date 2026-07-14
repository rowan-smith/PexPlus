package dev.rono.permissions.api.ladder;

public class LadderAlreadyExistsException extends RuntimeException {
    public LadderAlreadyExistsException(String message) {
        super(message);
    }
}
