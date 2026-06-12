package dev.rono.permissions.api.group;

import java.util.Optional;
import java.util.function.Predicate;

/** Group registry with explicit find/get/create/exists lifecycle. */
public interface GroupManager {

    Optional<Group> findGroup(String name);

    Group getGroup(String name) throws GroupNotFoundException;

    Group createGroup(String name) throws GroupAlreadyExistsException;

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
    int count(Predicate<Group> filter);
}
