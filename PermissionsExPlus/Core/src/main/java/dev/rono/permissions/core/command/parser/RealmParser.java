package dev.rono.permissions.core.command.parser;

import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import dev.rono.permissions.api.PermissionsExPlusApi;
import dev.rono.permissions.api.realm.Realm;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Queue;

public final class RealmParser<C> implements ArgumentParser<C, Realm> {

    private final PermissionsExPlusApi api;

    public RealmParser(final PermissionsExPlusApi api) {
        this.api = api;
    }

    @Override
    public @NonNull ArgumentParseResult<@NonNull Realm> parse(final @NonNull CommandContext<@NonNull C> commandContext, final @NonNull Queue<@NonNull String> inputQueue) {
        final String name = inputQueue.peek();
        if (name == null) {
            return ArgumentParseResult.failure(new IllegalArgumentException("No realm specified"));
        }

        final var realms = api.realms().all();
        for (final var realm : realms) {
            if (realm.name().equalsIgnoreCase(name)) {
                inputQueue.remove();
                return ArgumentParseResult.success(realm);
            }
        }

        return ArgumentParseResult.failure(new IllegalArgumentException("Unknown realm: " + name));
    }

    @Override
    public @NonNull List<@NonNull String> suggestions(final @NonNull CommandContext<C> commandContext, final @NonNull String input) {
        return api.realms().all().stream()
                .map(Realm::name)
                .filter(name -> name.toLowerCase().startsWith(input.toLowerCase()))
                .toList();
    }
}
