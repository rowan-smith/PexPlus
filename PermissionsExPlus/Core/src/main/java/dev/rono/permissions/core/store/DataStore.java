package dev.rono.permissions.core.store;

import java.util.Map;
import java.util.Optional;

public interface DataStore extends AutoCloseable {
    void open();

    Optional<String> get(String category, String key);

    Map<String, String> all(String category);

    void put(String category, String key, String payload);

    boolean remove(String category, String key);

    default void checkpoint() {}

    default boolean supportsCheckpoints() {
        return false;
    }

    String name();

    boolean persistent();

    @Override
    void close();
}
