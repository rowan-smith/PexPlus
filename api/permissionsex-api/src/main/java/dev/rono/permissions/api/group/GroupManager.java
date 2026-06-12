package dev.rono.permissions.api.group;

import java.util.Optional;

/** Group registry with explicit find/get/create/exists lifecycle. */
public interface GroupManager {

    Optional<Group> findGroup(String name);

    Group getGroup(String name) throws GroupNotFoundException;

    Group createGroup(String name) throws GroupAlreadyExistsException;

    boolean exists(String name);
}
