package dev.rono.permissions.api.subject;

import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.user.User;

/**
 * Factory for server-bound {@link SubjectServerContext} facades.
 *
 * <p>Every implementation here is a pure delegate: bind the realm via {@link SubjectWorldContexts}
 * and expose it as a server context. Do not add business logic in this class.</p>
 *
 * <p>On proxies, server names map to the same permission namespaces as {@link SubjectWorldContexts};
 * this factory exists for API clarity on proxy runtimes.</p>
 */
public final class SubjectServerContexts {
    private SubjectServerContexts() {}

    /**
     * Creates a server-bound context for {@code subject}.
     *
     * @param subject permission subject
     * @param server  backend server id on proxies, or a realm name; {@link dev.rono.permissions.api.world.Worlds#GLOBAL} for global
     * @return thin server projection; delegates all operations back to {@code subject}
     */
    public static SubjectServerContext subject(PermissionSubject subject, String server) {
        return (SubjectServerContext) SubjectWorldContexts.subject(subject, server);
    }

    /**
     * Creates a server-bound {@link UserServerContext} facade for {@code user}.
     *
     * @param user   user subject
     * @param server backend server id on proxies, or a realm name; {@link dev.rono.permissions.api.world.Worlds#GLOBAL} for global
     * @return thin server projection; delegates all operations back to {@code user}
     */
    public static UserServerContext user(User user, String server) {
        return (UserServerContext) SubjectWorldContexts.user(user, server);
    }

    /**
     * Creates a server-bound {@link GroupServerContext} facade for {@code group}.
     *
     * @param group  group subject
     * @param server backend server id on proxies, or a realm name; {@link dev.rono.permissions.api.world.Worlds#GLOBAL} for global
     * @return thin server projection; delegates all operations back to {@code group}
     */
    public static GroupServerContext group(Group group, String server) {
        return (GroupServerContext) SubjectWorldContexts.group(group, server);
    }
}
