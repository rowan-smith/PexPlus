package dev.rono.permissions.core.command.suggestions;

import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import dev.rono.permissions.core.command.CoreCloudCommandContext;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public final class CoreCloudSuggestions<C> {

    private final CoreCloudCommandContext<C> ctx;

    public CoreCloudSuggestions(final CoreCloudCommandContext<C> ctx) {
        this.ctx = ctx;
    }

    @Suggestions("pex-user")
    public @NonNull List<@NonNull String> suggestUser(final @NonNull CommandContext<C> context, final @NonNull String input) {
        return CoreCloudSuggestionHelper.matchSuggestions(
                ctx.api().users().all().stream().map(u -> u.name()).toList(),
                input
        );
    }

    @Suggestions("pex-group")
    public @NonNull List<@NonNull String> suggestGroup(final @NonNull CommandContext<C> context, final @NonNull String input) {
        return CoreCloudSuggestionHelper.matchSuggestions(
                ctx.api().groups().all().stream().map(g -> g.name()).toList(),
                input
        );
    }

    @Suggestions("pex-ladder")
    public @NonNull List<@NonNull String> suggestLadder(final @NonNull CommandContext<C> context, final @NonNull String input) {
        return CoreCloudSuggestionHelper.matchSuggestions(
                ctx.api().ladders().all().stream().map(l -> l.name()).toList(),
                input
        );
    }

    @Suggestions("pex-permission")
    public @NonNull List<@NonNull String> suggestPermission(final @NonNull CommandContext<C> context, final @NonNull String input) {
        return CoreCloudSuggestionHelper.matchSuggestions(List.of(), input);
    }
}
