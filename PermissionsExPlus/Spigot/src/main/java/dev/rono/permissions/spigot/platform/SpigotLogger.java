package dev.rono.permissions.spigot.platform;

import dev.rono.permissions.core.bridge.PlatformLogger;

import java.util.logging.Logger;

public class SpigotLogger implements PlatformLogger {
    private final Logger logger;

    public SpigotLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }
}
