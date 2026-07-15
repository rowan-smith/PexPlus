package dev.rono.permissions.core.logger;

import dev.rono.permissions.core.platform.PlatformLogger;

public final class LoggerManagerImpl {
    private final PlatformLogger logger;
    private final DebugLogger debug;
    private final AuditLogger audit;

    public LoggerManagerImpl(PlatformLogger logger, DebugLogger debug, AuditLogger audit) {
        this.logger = logger;
        this.debug = debug;
        this.audit = audit;
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void error(String message, Throwable error) {
        logger.error(message, error);
    }

    public void debug(String component, String message) {
        debug.log(component, message);
    }

    public void audit(String actor, String action) {
        audit.log(actor, action);
    }

    public AuditLogger audit() {
        return audit;
    }

    public DebugLogger debug() {
        return debug;
    }
}
