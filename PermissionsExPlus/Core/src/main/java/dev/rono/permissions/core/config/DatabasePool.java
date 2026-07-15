package dev.rono.permissions.core.config;

public record DatabasePool(int maximumPoolSize, int minimumIdle, long connectionTimeout, long maxLifetime) {
    public DatabasePool(int maximumPoolSize, int minimumIdle, long maxLifetime) {
        this(maximumPoolSize, minimumIdle, 5_000L, maxLifetime);
    }
}
