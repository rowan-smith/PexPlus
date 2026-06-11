package dev.rono.permissions.api.subject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Modern view of a permission user. */
public interface User extends PermissionSubject {

    @Override
    default SubjectType type() {
        return SubjectType.USER;
    }

    /** Parsed UUID when the identifier is UUID-shaped; empty for name-only records. */
    Optional<UUID> uniqueId();

    /** Group identifiers the user inherits in {@code world} (includes parents of parents when {@code inherit} is true). */
    List<String> groups(String world, boolean inherit);

    default List<String> groups(String world) {
        return groups(world, true);
    }

    boolean inGroup(String groupName, String world, boolean inherit);

    void addGroup(String groupName, String world);

    void addGroup(String groupName, String world, int lifetimeSeconds);

    void removeGroup(String groupName, String world);
}
