package dev.rono.permissions.core.model;

import dev.rono.permissions.api.ladder.Ladder;
import dev.rono.permissions.api.util.Identifiers;

import java.util.List;

public record LadderSnapshot(String name, List<String> groups) implements Ladder {
    public LadderSnapshot {
        name = Identifiers.ladder(name);
        groups = groups.stream().map(Identifiers::group).toList();
    }
}
