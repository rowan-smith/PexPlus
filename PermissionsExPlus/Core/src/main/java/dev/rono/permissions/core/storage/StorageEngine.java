package dev.rono.permissions.core.storage;


public interface StorageEngine {

    UserRepository users();

    GroupRepository groups();

    RealmRepository realms();

    LadderRepository ladders();

    void open();

    void close();
}
