package dev.rono.permissions.core.command.parser;

import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import dev.rono.permissions.api.PermissionsExPlusApi;
import dev.rono.permissions.api.group.Group;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Queue;

public final class GroupParser<C> implements ArgumentParser<C, Group> {

    private final PermissionsExPlusApi api;

    public GroupParser(final PermissionsExPlusApi api) {
        this.api = api;
    }

    @Override
    public @NonNull ArgumentParseResult<@NonNull Group> parse(final @NonNull CommandContext<@NonNull C> commandContext, final @NonNull Queue<@NonNull String> inputQueue) {
        final String name = inputQueue.peek();
        if (name == null) {
            return ArgumentParseResult.failure(new IllegalArgumentException("No group specified"));
        }

        final var groups = api.groups().all();
        for (final var group : groups) {
            if (group.name().equalsIgnoreCase(name)) {
                inputQueue.remove();
                return ArgumentParseResult.success(group);
            }
        }

        return ArgumentParseResult.failure(new IllegalArgumentException("Unknown group: " + name));
    }

    @Override
    public @NonNull List<@NonNull String> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull String input) {
        return api.groups().all().stream()
                .map(Group::name)
                .filter(name -> name.toLowerCase().startsWith(input.toLowerCase()))
                .toList();
    }
}
