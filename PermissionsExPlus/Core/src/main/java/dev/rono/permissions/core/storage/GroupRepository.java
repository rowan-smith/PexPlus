package dev.rono.permissions.core.storage;

import dev.rono.permissions.api.group.Group;

import java.util.Collection;
import java.util.Optional;


public interface GroupRepository {
    Optional<Group> find(String name);

    Group save(Group group);

    void delete(String name);

    Collection<Group> all();
}