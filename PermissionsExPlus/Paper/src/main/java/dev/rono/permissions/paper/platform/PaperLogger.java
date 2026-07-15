package dev.rono.permissions.paper.platform;

import dev.rono.permissions.core.platform.PlatformLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PaperLogger implements PlatformLogger {

    private final Logger logger;

    public PaperLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warning(message);
    }

    @Override
    public void error(String message, Throwable error) {
        logger.log(Level.SEVERE, message, error);
    }
}
