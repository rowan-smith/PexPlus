package dev.rono.permissions.core.cache;

import dev.rono.permissions.api.realm.Realm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class RealmCache {

    private final Map<String, Realm> realms = new ConcurrentHashMap<>();

    public Realm get(String name) {
        return realms.get(name);
    }

    public void put(Realm realm) {
        realms.put(realm.name(), realm);
    }

    public void remove(String name) {
        realms.remove(name);
    }
}
