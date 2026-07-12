package dev.rono.permissions.api.group;

import java.util.Optional;
import java.util.function.Predicate;

public interface GroupManager {
    /**
     * Looks up a persisted group without creating an in-memory record.
     *
     * @param name group identifier
     * @return the group when stored in the backend; empty if absent
     */
    Optional<PermissionGroup> find(String name);

    /**
     * Returns a persisted group by name.
     *
     * @param name group identifier
     * @return live group adapter
     * @throws GroupNotFoundException if no backend record exists for {@code name}
     */
    PermissionGroup load(String name) throws GroupNotFoundException;

    /**
     * Creates a new group record.
     *
     * @param name group identifier
     * @return live group adapter for the new record
     * @throws GroupAlreadyExistsException if a group with {@code name} already exists
     */
    PermissionGroup create(String name) throws GroupAlreadyExistsException;

    /**
     * Permanently deletes a group record.
     *
     * @param name group identifier
     * @throws GroupNotFoundException if no backend record exists for {@code name}
     */
    void delete(String name);

    /**
     * Reports whether a group record exists.
     *
     * @param name group identifier
     * @return {@code true} if persisted or already materialized in memory
     */
    boolean exists(String name);

    /**
     * Returns the number of group records stored in the active backend.
     *
     * @return total persisted group count
     */
    int count();

    /**
     * Returns how many persisted groups match {@code filter}.
     *
     * @param filter predicate applied to each stored group; must not be {@code null}
     * @return count of groups for which the predicate is {@code true}
     */
    int count(Predicate<PermissionGroup> filter);
}
