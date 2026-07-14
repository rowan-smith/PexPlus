package dev.rono.permissions.core.ladder;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.ladder.Ladder;

import java.util.List;
import java.util.OptionalInt;


public final class LadderImpl implements Ladder {

    private final String name;

    private final List<Group> groups;

    public LadderImpl(String name, List<Group> groups) {
        this.name = name;
        this.groups = groups;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<Group> groups() {
        return groups;
    }

    @Override
    public void add(Group group) {

    }

    @Override
    public void remove(Group group) {

    }

    @Override
    public OptionalInt position(Group group) {
        return OptionalInt.empty();
    }
}