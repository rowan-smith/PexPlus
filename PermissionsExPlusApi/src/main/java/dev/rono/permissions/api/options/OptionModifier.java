package dev.rono.permissions.api.options;

import dev.rono.permissions.api.context.ContextKeys;
import dev.rono.permissions.api.context.ContextSet;

import java.time.Duration;
import java.util.Objects;

public interface OptionModifier<Self extends OptionModifier<Self>> {

    /**
     * Adds or replaces an explicit option.
     *
     * <p>
     * An option is identified by its key and exact context set.
     * If a matching option already exists, its value is replaced.
     * </p>
     */
    Self setOption(OptionNode option);

    default Self setOption(String key, String value) {
        return setOption(OptionNode.builder()
                .key(key)
                .value(value)
                .build());
    }

    default Self setOption(String key, String value, ContextSet contexts) {
        Objects.requireNonNull(contexts, "contexts");

        return setOption(OptionNode.builder()
                .key(key)
                .value(value)
                .contexts(contexts)
                .build());
    }

    default Self setOption(String key, String value, ContextKeys contextKey, String contextValue) {
        Objects.requireNonNull(contextKey, "contextKey");

        return setOption(key, value, ContextSet.builder().add(contextKey, contextValue).build());
    }

    default Self setTemporaryOption(String key, String value, Duration duration) {
        return setOption(OptionNode.builder()
                .key(key)
                .value(value)
                .duration(duration)
                .build());
    }

    default Self setTemporaryOption(
            String key,
            String value,
            ContextSet contexts,
            Duration duration) {
        Objects.requireNonNull(contexts, "contexts");

        return setOption(OptionNode.builder()
                .key(key)
                .value(value)
                .contexts(contexts)
                .duration(duration)
                .build());
    }

    default Self setTemporaryOption(String key, String value, ContextKeys contextKey, String contextValue, Duration duration) {
        Objects.requireNonNull(contextKey, "contextKey");

        return setTemporaryOption(key, value, ContextSet.builder().add(contextKey, contextValue).build(), duration);
    }

    Self removeOption(String key, ContextSet contexts);

    default Self removeOption(String key, ContextKeys contextKey, String contextValue) {
        Objects.requireNonNull(contextKey, "contextKey");

        return removeOption(key, ContextSet.builder().add(contextKey, contextValue).build());
    }

    /** Removes this option key from every context. */
    Self removeOptions(String key);

    /** Removes every explicit option. */
    Self clearOptions();

    /** Removes all explicit options with exactly these contexts. */
    Self clearOptions(ContextSet contexts);

    default Self clearOptions(ContextKeys contextKey, String contextValue) {
        Objects.requireNonNull(contextKey, "contextKey");

        return clearOptions(ContextSet.builder().add(contextKey, contextValue).build());
    }

    default Self removeOption(String key) {
        Objects.requireNonNull(key, "key");

        return removeOption(key, ContextSet.empty());
    }

    default Self removeOption(OptionNode option) {
        Objects.requireNonNull(option, "option");

        return removeOption(option.key(), option.contexts());
    }

    default Self setPrefix(String prefix) {
        Objects.requireNonNull(prefix, "prefix");

        return setOption(OptionKeys.PREFIX, prefix);
    }

    default Self setPrefix(String prefix, ContextSet contexts) {
        Objects.requireNonNull(prefix, "prefix");

        Objects.requireNonNull(contexts, "contexts");

        return setOption(OptionKeys.PREFIX, prefix, contexts);
    }

    default Self setPrefix(String prefix, ContextKeys contextKey, String contextValue) {
        return setPrefix(prefix, ContextSet.builder().add(contextKey, contextValue).build());
    }

    default Self setTemporaryPrefix(String prefix, Duration duration) {
        return setTemporaryOption(OptionKeys.PREFIX, prefix, duration);
    }

    default Self setTemporaryPrefix(String prefix, ContextSet contexts, Duration duration) {
        return setTemporaryOption(OptionKeys.PREFIX, prefix, contexts, duration);
    }

    default Self setTemporaryPrefix(String prefix, ContextKeys contextKey, String contextValue, Duration duration) {
        return setTemporaryPrefix(prefix, ContextSet.builder().add(contextKey, contextValue).build(), duration);
    }

    default Self removePrefix() {
        return removeOption(OptionKeys.PREFIX);
    }

    default Self removePrefix(ContextSet contexts) {
        Objects.requireNonNull(contexts, "contexts");

        return removeOption(OptionKeys.PREFIX, contexts);
    }

    default Self removePrefix(ContextKeys contextKey, String contextValue) {
        return removePrefix(ContextSet.builder().add(contextKey, contextValue).build());
    }

    default Self setSuffix(String suffix) {
        Objects.requireNonNull(suffix, "suffix");

        return setOption(OptionKeys.SUFFIX, suffix);
    }

    default Self setSuffix(String suffix, ContextSet contexts) {
        Objects.requireNonNull(suffix, "suffix");

        Objects.requireNonNull(contexts, "contexts");

        return setOption(OptionKeys.SUFFIX, suffix, contexts);
    }

    default Self setSuffix(String suffix, ContextKeys contextKey, String contextValue) {
        return setSuffix(suffix, ContextSet.builder().add(contextKey, contextValue).build());
    }

    default Self setTemporarySuffix(String suffix, Duration duration) {
        return setTemporaryOption(OptionKeys.SUFFIX, suffix, duration);
    }

    default Self setTemporarySuffix(String suffix, ContextSet contexts, Duration duration) {
        return setTemporaryOption(OptionKeys.SUFFIX, suffix, contexts, duration);
    }

    default Self setTemporarySuffix(String suffix, ContextKeys contextKey, String contextValue, Duration duration) {
        return setTemporarySuffix(suffix, ContextSet.builder().add(contextKey, contextValue).build(), duration);
    }

    default Self removeSuffix() {
        return removeOption(OptionKeys.SUFFIX);
    }

    default Self removeSuffix(ContextSet contexts) {
        Objects.requireNonNull(contexts, "contexts");

        return removeOption(OptionKeys.SUFFIX, contexts);
    }

    default Self removeSuffix(ContextKeys contextKey, String contextValue) {
        return removeSuffix(ContextSet.builder().add(contextKey, contextValue).build());
    }
}
