package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.permission.PermissionContext;
import dev.rono.permissions.api.user.User;

/**
 * Factory for {@link PermissionContext}-bound {@link SubjectContext} facades.
 */
public final class SubjectContexts {
    private SubjectContexts() {}

    /** @return context-bound projection of {@code subject} */
    public static SubjectContext subject(PermissionSubject subject, PermissionContext context) {
        return SubjectWorldContexts.subject(subject, context);
    }

    /** @return context-bound user projection */
    public static UserWorldContext user(User user, PermissionContext context) {
        return SubjectWorldContexts.user(user, context);
    }

    /** @return context-bound group projection */
    public static GroupWorldContext group(Group group, PermissionContext context) {
        return SubjectWorldContexts.group(group, context);
    }
}
