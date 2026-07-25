package dev.rono.permissions.api.context;

public interface ContextBuilder {
    ContextBuilder add(String key, String value);

    default ContextBuilder add(ContextKeys key, String value) {
        return add(key.key(), value);
    }

    ContextBuilder remove(String key, String value);

    default ContextBuilder remove(ContextKeys key, String value) {
        return remove(key.key(), value);
    }

    ContextBuilder remove(String key);

    default ContextBuilder remove(ContextKeys key) {
        return remove(key.key());
    }

    ContextBuilder set(String key, String value);

    default ContextBuilder set(ContextKeys key, String value) {
        return set(key.key(), value);
    }

    ContextBuilder clear();

    ContextSet build();
}
