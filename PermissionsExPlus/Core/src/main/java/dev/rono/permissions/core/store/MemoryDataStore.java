package dev.rono.permissions.core.store;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class MemoryDataStore implements DataStore {
    private final Map<String, Map<String, String>> data = new ConcurrentHashMap<>();

    @Override
    public void open() {}

    @Override
    public Optional<String> get(String category, String key) {
        return Optional.ofNullable(data.getOrDefault(category, Map.of()).get(key));
    }

    @Override
    public Map<String, String> all(String category) {
        return Map.copyOf(data.getOrDefault(category, Map.of()));
    }

    @Override
    public void put(String category, String key, String payload) {
        data.computeIfAbsent(category, ignored -> new ConcurrentHashMap<>()).put(key, payload);
    }

    @Override
    public boolean remove(String category, String key) {
        var values = data.get(category);
        return values != null && values.remove(key) != null;
    }

    @Override
    public String name() {
        return "Memory";
    }

    @Override
    public boolean persistent() {
        return false;
    }

    @Override
    public void close() {}
}
