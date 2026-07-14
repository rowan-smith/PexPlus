package dev.rono.permissions.core.storage;

import dev.rono.permissions.api.ladder.Ladder;

import java.util.Collection;
import java.util.Optional;


public interface LadderRepository {
    Optional<Ladder> find(String name);

    Ladder save(Ladder ladder);

    void delete(String name);

    Collection<Ladder> all();
}