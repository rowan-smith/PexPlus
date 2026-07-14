package dev.rono.permissions.core.command.parser;

import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import dev.rono.permissions.api.PermissionsExPlusApi;
import dev.rono.permissions.api.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Queue;

public final class UserParser<C> implements ArgumentParser<C, User> {

    private final PermissionsExPlusApi api;

    public UserParser(final PermissionsExPlusApi api) {
        this.api = api;
    }

    @Override
    public @NonNull ArgumentParseResult<@NonNull User> parse(final @NonNull CommandContext<@NonNull C> commandContext, final @NonNull Queue<@NonNull String> inputQueue) {
        final String name = inputQueue.peek();
        if (name == null) {
            return ArgumentParseResult.failure(new IllegalArgumentException("No user specified"));
        }

        final var users = api.users().all();
        for (final var user : users) {
            if (user.name().equalsIgnoreCase(name)) {
                inputQueue.remove();
                return ArgumentParseResult.success(user);
            }
        }

        return ArgumentParseResult.failure(new IllegalArgumentException("Unknown user: " + name));
    }

    @Override
    public @NonNull List<@NonNull String> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull String input) {
        return api.users().all().stream()
                .map(User::name)
                .filter(name -> name.toLowerCase().startsWith(input.toLowerCase()))
                .toList();
    }
}
