package dev.rono.permissions.core.realm;

import dev.rono.permissions.api.realm.*;
import dev.rono.permissions.core.storage.RealmRepository;

import java.util.Collection;
import java.util.Optional;

public final class RealmManagerImpl implements RealmManager {

    private final RealmRepository repository;

    public RealmManagerImpl(RealmRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Realm> find(String name) {
        return repository.find(name);
    }

    @Override
    public Realm load(String name) {
        return repository.find(name).orElseThrow(() -> new RealmNotFoundException(name));
    }

    @Override
    public Realm create(String name) {

        if (repository.find(name).isPresent()) {
            throw new RealmAlreadyExistsException(name);
        }

        Realm realm = new RealmImpl(name);

        repository.save(realm);

        return realm;
    }

    @Override
    public void delete(String name) {
        repository.delete(name);
    }

    @Override
    public Collection<Realm> all() {
        return repository.all();
    }
}