package dev.rono.permissions.api.config;

public interface ConfigurationManager {
    boolean preloadOnJoin();

    boolean vaultEnabled();

    boolean placeholderApiEnabled();
}
