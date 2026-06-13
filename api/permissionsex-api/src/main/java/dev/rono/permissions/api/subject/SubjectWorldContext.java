package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.world.Worlds;
import java.util.List;
import java.util.Map;

/**
 * Legacy world-scoped projection; prefer {@link SubjectContext}.
 */
public interface SubjectWorldContext extends SubjectContext {

    /** @return legacy world/realm label */
    String world();
}
