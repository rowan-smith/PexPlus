package dev.rono.permissions.core.command.suggestions;

import java.util.Collection;
import java.util.List;

public final class CoreCloudSuggestionHelper {

    private static final int MAX_SUGGESTIONS = 50;

    public static List<String> matchSuggestions(final Collection<String> candidates, final String input) {
        if (candidates.isEmpty()) {
            return List.of();
        }

        if (input.isEmpty()) {
            return candidates.stream().limit(MAX_SUGGESTIONS).toList();
        }

        final var lower = input.toLowerCase();
        return candidates.stream()
                .filter(s -> s != null && s.toLowerCase().contains(lower))
                .limit(MAX_SUGGESTIONS)
                .toList();
    }

    public static List<String> matchPermissionSuggestions(final Collection<String> candidates, final String input) {
        return matchSuggestions(candidates, input);
    }

    public static List<String> matchCsvSuggestions(final Collection<String> candidates, final String input) {
        final var last = input.lastIndexOf(',');
        final var prefix = last >= 0 ? input.substring(0, last + 1) + ' ' : "";
        final var partial = last >= 0 ? input.substring(last + 1).trim() : input.trim();

        if (partial.isEmpty()) {
            return candidates.stream().limit(MAX_SUGGESTIONS).map(s -> prefix + s).toList();
        }

        final var lower = partial.toLowerCase();
        return candidates.stream()
                .filter(s -> s != null && s.toLowerCase().contains(lower))
                .limit(MAX_SUGGESTIONS)
                .map(s -> prefix + s)
                .toList();
    }
}
