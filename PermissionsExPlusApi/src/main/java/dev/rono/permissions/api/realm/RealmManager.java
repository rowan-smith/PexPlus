package dev.rono.permissions.api.realm;

import java.util.Collection;
import java.util.Optional;

public interface RealmManager {
    Optional<Realm> find(String name);

    Realm load(String name);

    Realm create(String name);

    void delete(String name);

    Collection<Realm> all();
}