package dev.rono.permissions.core.realm;

import dev.rono.permissions.api.realm.Realm;

public final class RealmImpl implements Realm {

    private final String name;

    public RealmImpl(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "Realm[" + name + "]";
    }
}