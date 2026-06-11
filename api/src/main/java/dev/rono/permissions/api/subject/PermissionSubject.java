package dev.rono.permissions.api.subject;

import java.util.List;
import java.util.Map;

/**
 * Shared permission-subject operations for users and groups.
 *
 * <p>{@code world} is {@code null} or empty for the global context (classic PEX {@code null} world).</p>
 */
public interface PermissionSubject {

    SubjectType type();

    String identifier();

    String name();

    boolean virtual();

    boolean has(String permission, String world);

    /** Direct assignments in the given world (not inherited). */
    List<String> permissions(String world);

    /** Effective permissions after inheritance in the given world. */
    List<String> effectivePermissions(String world);

    void addPermission(String permission, String world);

    void removePermission(String permission, String world);

    void setPermissions(List<String> permissions, String world);

    void addTimedPermission(String permission, String world, int lifetimeSeconds);

    void removeTimedPermission(String permission, String world);

    List<String> timedPermissions(String world);

    String prefix(String world);

    String suffix(String world);

    void setPrefix(String prefix, String world);

    void setSuffix(String suffix, String world);

    String option(String key, String world);

    void setOption(String key, String value, String world);

    Map<String, String> options(String world);

    void save();

    void delete();
}
