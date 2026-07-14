package dev.rono.permissions.core.storage.memory;


import dev.rono.permissions.core.storage.*;


public final class MemoryStorageEngine implements StorageEngine {

    private final MemoryUserRepository users = new MemoryUserRepository();

    private final MemoryGroupRepository groups = new MemoryGroupRepository();

    private final MemoryRealmRepository realms = new MemoryRealmRepository();

    private final MemoryLadderRepository ladders = new MemoryLadderRepository();

    @Override
    public UserRepository users() {
        return users;
    }

    @Override
    public GroupRepository groups() {
        return groups;
    }

    @Override
    public RealmRepository realms() {
        return realms;
    }

    @Override
    public LadderRepository ladders() {
        return ladders;
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {
        users.clear();
        groups.clear();
        realms.clear();
        ladders.clear();
    }
}