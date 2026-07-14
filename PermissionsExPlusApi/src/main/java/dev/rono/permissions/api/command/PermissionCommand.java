package dev.rono.permissions.api.command;

public interface PermissionCommand {
    String name();

    void execute(CommandContext context, String[] args);
}
