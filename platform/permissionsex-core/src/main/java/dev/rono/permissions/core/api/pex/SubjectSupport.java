package dev.rono.permissions.core.api.pex;

import dev.rono.permissions.api.RankingException;
import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.user.User;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;

final class SubjectSupport {

    private SubjectSupport() {}

    static PermissionUser requireUser(User user) {
        if (user instanceof UserImpl impl) {
            return impl.delegate();
        }
        throw new IllegalArgumentException("Unsupported User implementation: " + user.getClass().getName());
    }

    static PermissionUser optionalUser(User user) {
        return user == null ? null : requireUser(user);
    }

    static RankingException toRankingException(ru.tehkode.permissions.exceptions.RankingException legacy) {
        return new RankingException(legacy.getMessage(), legacy);
    }

    static Group wrapGroup(PermissionGroup group, dev.rono.permissions.core.DefaultPermissionManager manager) {
        return new GroupImpl(group.getIdentifier(), group, manager);
    }
}
