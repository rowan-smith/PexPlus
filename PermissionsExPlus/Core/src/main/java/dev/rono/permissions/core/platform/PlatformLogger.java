package dev.rono.permissions.core.platform;

public interface PlatformLogger {
    void info(String message);

    void warn(String message);

    void error(String message, Throwable error);
}
