package dev.rono.permissions.core.storage.memory;

import dev.rono.permissions.api.ladder.Ladder;
import dev.rono.permissions.core.storage.LadderRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public final class MemoryLadderRepository implements LadderRepository {

    private final Map<String, Ladder> ladders = new ConcurrentHashMap<>();

    @Override
    public Optional<Ladder> find(String name) {
        return Optional.ofNullable(ladders.get(name));
    }

    @Override
    public Ladder save(Ladder ladder) {
        ladders.put(ladder.name(), ladder);

        return ladder;
    }

    @Override
    public void delete(String name) {
        ladders.remove(name);
    }

    @Override
    public Collection<Ladder> all() {
        return Collections.unmodifiableCollection(ladders.values());
    }

    public void clear() {
        ladders.clear();
    }
}