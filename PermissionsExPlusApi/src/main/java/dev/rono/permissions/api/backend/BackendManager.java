package dev.rono.permissions.api.backend;

import java.util.Collection;

/** Read-only discovery of the active and supported storage backends. */
public interface BackendManager {
    Backend current();

    Collection<Backend> available();
}
