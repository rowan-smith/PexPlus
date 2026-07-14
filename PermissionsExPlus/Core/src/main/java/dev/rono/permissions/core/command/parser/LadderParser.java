package dev.rono.permissions.core.command.parser;

import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import dev.rono.permissions.api.PermissionsExPlusApi;
import dev.rono.permissions.api.ladder.Ladder;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Queue;

public final class LadderParser<C> implements ArgumentParser<C, Ladder> {

    private final PermissionsExPlusApi api;

    public LadderParser(final PermissionsExPlusApi api) {
        this.api = api;
    }

    @Override
    public @NonNull ArgumentParseResult<@NonNull Ladder> parse(final @NonNull CommandContext<@NonNull C> commandContext, final @NonNull Queue<@NonNull String> inputQueue) {
        final String name = inputQueue.peek();
        if (name == null) {
            return ArgumentParseResult.failure(new IllegalArgumentException("No ladder specified"));
        }

        final var ladders = api.ladders().all();
        for (final var ladder : ladders) {
            if (ladder.name().equalsIgnoreCase(name)) {
                inputQueue.remove();
                return ArgumentParseResult.success(ladder);
            }
        }

        return ArgumentParseResult.failure(new IllegalArgumentException("Unknown ladder: " + name));
    }

    @Override
    public @NonNull List<@NonNull String> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull String input) {
        return api.ladders().all().stream()
                .map(Ladder::name)
                .filter(name -> name.toLowerCase().startsWith(input.toLowerCase()))
                .toList();
    }
}
