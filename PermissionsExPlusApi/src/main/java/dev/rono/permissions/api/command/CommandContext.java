package dev.rono.permissions.api.command;

public interface CommandContext {
    String senderName();

    boolean hasPermission(String permission);

    void sendMessage(String message);
}
