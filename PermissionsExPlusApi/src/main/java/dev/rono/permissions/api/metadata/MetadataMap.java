package dev.rono.permissions.api.metadata;

public interface MetadataMap {
    Object get(String key);

    void set(String key, Object value);

    void remove(String key);
}
