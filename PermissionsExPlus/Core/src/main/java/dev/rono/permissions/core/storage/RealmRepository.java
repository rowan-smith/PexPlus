package dev.rono.permissions.core.storage;

import dev.rono.permissions.api.realm.Realm;

import java.util.Collection;
import java.util.Optional;


public interface RealmRepository {
    Optional<Realm> find(String name);

    Realm save(Realm realm);

    void delete(String name);

    Collection<Realm> all();
}