package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.permission.PermissionContext;
import java.util.List;
import java.util.Map;

/**
 * Context-scoped projection of a {@link PermissionSubject}.
 *
 * <p>Obtained via {@link PermissionSubject#inContext(PermissionContext)}. Every method applies to the
 * bound {@link #context()} without repeating scope arguments.</p>
 *
 * <p><strong>Thin facade invariant:</strong> implementations delegate to the underlying subject with the
 * bound context fixed; no duplicated resolution or persistence logic.</p>
 */
public interface SubjectContext {

    /** @return bound permission scope */
    PermissionContext context();

    /** @return underlying permission subject */
    PermissionSubject subject();

    boolean hasPermission(String permission);

    default boolean has(String permission) {
        return hasPermission(permission);
    }

    List<String> permissions();

    List<String> effectivePermissions();

    void addPermission(String permission);

    void removePermission(String permission);

    void setPermissions(List<String> permissions);

    void addTimedPermission(String permission, int lifetimeSeconds);

    void removeTimedPermission(String permission);

    List<String> timedPermissions();

    List<TimedPermissionEntry> timedPermissionEntries();

    int timedPermissionRemainingSeconds(String permission);

    boolean hasTimedPermission(String permission);

    String prefix();

    String suffix();

    void setPrefix(String prefix);

    void setSuffix(String suffix);

    String option(String key);

    void setOption(String key, String value);

    Map<String, String> options();
}
