package dev.rono.permissions.core.metadata;

import dev.rono.permissions.api.metadata.MetadataMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public final class MetadataMapImpl implements MetadataMap {

    private final Map<String,Object> values = new ConcurrentHashMap<>();

    @Override
    public Object get(String key) {
        return values.get(key);
    }

    @Override
    public void set(String key, Object value) {
        values.put(key, value);
    }

    @Override
    public void remove(String key) {
        values.remove(key);
    }
}