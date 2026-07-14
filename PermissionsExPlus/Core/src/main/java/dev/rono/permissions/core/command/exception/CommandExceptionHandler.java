package dev.rono.permissions.core.command.exception;

import cloud.commandframework.CommandManager;
import cloud.commandframework.exceptions.ArgumentParseException;
import cloud.commandframework.exceptions.CommandExecutionException;
import cloud.commandframework.exceptions.NoPermissionException;
import cloud.commandframework.exceptions.NoSuchCommandException;
import dev.rono.permissions.api.group.GroupAlreadyExistsException;
import dev.rono.permissions.api.group.GroupNotFoundException;
import dev.rono.permissions.api.ladder.LadderNotFoundException;
import dev.rono.permissions.api.realm.RealmNotFoundException;
import dev.rono.permissions.api.user.UserNotFoundException;

import java.util.function.BiConsumer;

public final class CommandExceptionHandler {

    public static <C> void register(final CommandManager<C> manager, final BiConsumer<C, String> messageSender) {
        manager.registerExceptionHandler(ArgumentParseException.class, (sender, exception) -> {
            final var cause = exception.getCause();
            final var message = switch (cause) {
                case UserNotFoundException e -> "§c" + e.getMessage();
                case GroupNotFoundException e -> "§c" + e.getMessage();
                case RealmNotFoundException e -> "§c" + e.getMessage();
                case LadderNotFoundException e -> "§c" + e.getMessage();
                case GroupAlreadyExistsException e -> "§c" + e.getMessage();
                case IllegalArgumentException e -> "§cInvalid command argument.";
                default -> "§c" + cause.getMessage();
            };

            messageSender.accept(sender, message);
        });

        manager.registerExceptionHandler(NoPermissionException.class, (sender, exception) ->
                messageSender.accept(sender, "§cYou don't have permission to use this command.")
        );

        manager.registerExceptionHandler(NoSuchCommandException.class, (sender, exception) ->
                messageSender.accept(sender, "§cUnknown command. Use /pex help for assistance.")
        );

        manager.registerExceptionHandler(CommandExecutionException.class, (sender, exception) -> {
            final var cause = exception.getCause();
            final var message = cause != null ? cause.getMessage() : "An error occurred";

            messageSender.accept(sender, "§c" + message);
        });
    }
}
