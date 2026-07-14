package dev.rono.permissions.core.storage.memory;

import dev.rono.permissions.api.realm.Realm;
import dev.rono.permissions.core.storage.RealmRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public final class MemoryRealmRepository implements RealmRepository {

    private final Map<String, Realm> realms = new ConcurrentHashMap<>();

    @Override
    public Optional<Realm> find(String name) {
        return Optional.ofNullable(realms.get(name));
    }

    @Override
    public Realm save(Realm realm) {
        realms.put(realm.name(), realm);

        return realm;
    }

    @Override
    public void delete(String name) {
        realms.remove(name);
    }

    @Override
    public Collection<Realm> all() {
        return Collections.unmodifiableCollection(realms.values());
    }

    public void clear() {
        realms.clear();
    }
}