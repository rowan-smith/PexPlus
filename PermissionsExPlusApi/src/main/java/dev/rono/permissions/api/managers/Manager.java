package dev.rono.permissions.api.managers;

public interface Manager<Identifier, Type, Modifier> extends ModifierManager<Identifier, Type, Modifier> {

    CacheManager<Identifier, Type> cache();

    StorageManager<Identifier, Type> storage();
}
