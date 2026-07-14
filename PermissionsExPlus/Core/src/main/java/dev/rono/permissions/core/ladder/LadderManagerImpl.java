package dev.rono.permissions.core.ladder;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.ladder.Ladder;
import dev.rono.permissions.api.ladder.LadderManager;
import dev.rono.permissions.api.ladder.PromotionResult;
import dev.rono.permissions.api.realm.Realm;
import dev.rono.permissions.api.user.User;
import dev.rono.permissions.core.storage.LadderRepository;

import java.util.Collection;
import java.util.Optional;

public final class LadderManagerImpl implements LadderManager {

    private final LadderRepository repository;

    public LadderManagerImpl(LadderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Ladder> find(String name) {
        return repository.find(name);
    }

    @Override
    public Ladder load(String name) {
        return repository.find(name).orElse(null);
    }

    @Override
    public Ladder create(String name) {
        return null;
    }

    @Override
    public void delete(String name) {
        repository.delete(name);
    }

    @Override
    public PromotionResult promote(User user, Ladder ladder, Realm realm) {
        return null;
    }

    @Override
    public PromotionResult demote(User user, Ladder ladder, Realm realm) {
        return null;
    }

    @Override
    public void addGroup(Ladder ladder, Group group) {
        ladder.add(group);
    }

    @Override
    public void removeGroup(Ladder ladder, Group group) {
        ladder.remove(group);
    }

    @Override
    public Collection<Ladder> all() {
        return repository.all();
    }
}
