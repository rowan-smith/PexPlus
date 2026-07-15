package dev.rono.permissions.api.context;

public interface ContextBuilder {
    ContextBuilder add(String key, String value);

    ContextBuilder remove(String key, String value);

    ContextBuilder remove(String key);

    ContextBuilder set(String key, String value);

    ContextBuilder clear();

    ContextSet build();
}
