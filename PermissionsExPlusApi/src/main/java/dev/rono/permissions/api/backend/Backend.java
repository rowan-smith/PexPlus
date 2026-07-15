package dev.rono.permissions.api.backend;

/** Read-only description of a supported storage backend. */
public interface Backend {
    String name();

    boolean persistent();

    BackendStatus status();
}
