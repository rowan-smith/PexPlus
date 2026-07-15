package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.group.Group;

import java.util.Optional;
import java.util.Set;

/**
 * An immutable user-specific snapshot including effective group information.
 */
public interface ResolvedUserData extends ResolvedData {

    Set<Group> groups();

    Optional<Group> primaryGroup();
}
