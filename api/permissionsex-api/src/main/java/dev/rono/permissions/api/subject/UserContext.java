package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.user.User;
import java.util.List;

/**
 * Context-scoped view of a {@link User} including group membership operations.
 */
public interface UserContext extends SubjectWorldContext {

    @Override
    User subject();

    List<String> groups(boolean inherit);

    default List<String> groups() {
        return groups(true);
    }

    boolean inGroup(String groupName, boolean inherit);

    default boolean inGroup(String groupName) {
        return inGroup(groupName, true);
    }

    void addGroup(String groupName);

    void addGroup(String groupName, int lifetimeSeconds);

    void removeGroup(String groupName);

    List<TimedGroupMembership> timedGroupMemberships();

    int groupMembershipRemainingSeconds(String groupName);
}
