package dev.rono.permissions.api.ladder;

import dev.rono.permissions.api.group.Group;

import java.util.List;
import java.util.OptionalInt;

public interface Ladder {
    String name();

    List<Group> groups();

    void add(Group group);

    void remove(Group group);

    OptionalInt position(Group group);
}