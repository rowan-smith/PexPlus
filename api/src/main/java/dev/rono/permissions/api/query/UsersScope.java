package dev.rono.permissions.api.query;

import dev.rono.permissions.api.service.PermissionServiceBridge;
import dev.rono.permissions.api.subject.User;
import dev.rono.permissions.api.subject.UserWorldContext;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * User registry — obtain via {@link PermissionQuery#users()}.
 *
 * <h2>Resolve vs find</h2>
 * <ul>
 *   <li><strong>Resolve</strong> ({@link #resolve(UUID)}, {@link #resolve(String)},
 *       {@link #inWorld(UUID, String)}, {@link WorldScope#user(UUID)}): obtains a live
 *       {@link User} or {@link UserWorldContext}, creating or materializing a backend record when
 *       the subject does not yet exist. Safe for edits and permission grants.</li>
 *   <li><strong>Find</strong> ({@link #find(UUID)}, {@link #find(String)},
 *       {@link #findInWorld(UUID, String)}, {@link WorldScope#findUser(UUID)}): queries persisted
 *       data only; returns {@link Optional#empty()} when no record is stored. Use for read-only
 *       checks where creating a subject would be incorrect.</li>
 * </ul>
 *
 * <pre>{@code
 * int n = pex.query().users().count();
 * pex.query().users().resolve(uuid).inWorld(world).addPermission("node");
 * pex.query().users().find(uuid).get().ifPresent(u -> log.info(u.identifier()));
 * }</pre>
 */
public final class UsersScope {

    private final PermissionServiceBridge service;

    UsersScope(PermissionServiceBridge service) {
        this.service = service;
    }

    /**
     * Returns the number of user records in the active backend.
     *
     * @return persisted user count
     */
    public int count() {
        return service.userCount();
    }

    /**
     * Returns every user identifier stored in the active backend.
     *
     * @return set of persisted user identifiers
     */
    public Set<String> identifiers() {
        return service.userIdentifiers();
    }

    /**
     * Removes a user record from the active backend.
     *
     * @param identifier user name or UUID string to delete
     */
    public void delete(String identifier) {
        service.deleteUser(identifier);
    }

    /**
     * Begins a resolve chain for a user identified by UUID.
     *
     * <p>Materializes a backend record when none exists yet. Chain with {@link ResolvedUser#inWorld(String)}
     * or {@link ResolvedUser#global()} to obtain a world context.</p>
     *
     * @param uuid player UUID
     * @return a fluent handle for further resolution
     */
    public ResolvedUser resolve(UUID uuid) {
        return new ResolvedUser(SubjectRefs.user(service, uuid, null));
    }

    /**
     * Begins a resolve chain for a user identified by name or UUID string.
     *
     * <p>Materializes a backend record when none exists yet. Chain with {@link ResolvedUser#inWorld(String)}
     * or {@link ResolvedUser#global()} to obtain a world context.</p>
     *
     * @param identifier user name or UUID string
     * @return a fluent handle for further resolution
     */
    public ResolvedUser resolve(String identifier) {
        return new ResolvedUser(SubjectRefs.user(service, null, identifier));
    }

    /**
     * Begins a find chain for a user identified by UUID.
     *
     * <p>Does not create records. Returns empty at each step when the user is not persisted.</p>
     *
     * @param uuid player UUID
     * @return a fluent handle for optional lookup
     */
    public FoundUser find(UUID uuid) {
        return new FoundUser(SubjectRefs.user(service, uuid, null));
    }

    /**
     * Begins a find chain for a user identified by name or UUID string.
     *
     * <p>Does not create records. Returns empty at each step when the user is not persisted.</p>
     *
     * @param identifier user name or UUID string
     * @return a fluent handle for optional lookup
     */
    public FoundUser find(String identifier) {
        return new FoundUser(SubjectRefs.user(service, null, identifier));
    }

    /**
     * Resolves a user and returns a world-bound context in one step.
     *
     * <p>Equivalent to {@code resolve(uuid).inWorld(world)}.</p>
     *
     * @param uuid player UUID
     * @param world target world name, or {@code null} for global
     * @return a {@link UserWorldContext} for the resolved user in the given world
     */
    public UserWorldContext inWorld(UUID uuid, String world) {
        return resolve(uuid).inWorld(world);
    }

    /**
     * Resolves a user and returns a world-bound context in one step.
     *
     * <p>Equivalent to {@code resolve(identifier).inWorld(world)}.</p>
     *
     * @param identifier user name or UUID string
     * @param world target world name, or {@code null} for global
     * @return a {@link UserWorldContext} for the resolved user in the given world
     */
    public UserWorldContext inWorld(String identifier, String world) {
        return resolve(identifier).inWorld(world);
    }

    /**
     * Looks up a persisted user and returns a world-bound context when present.
     *
     * <p>Equivalent to {@code find(uuid).inWorld(world)}.</p>
     *
     * @param uuid player UUID
     * @param world target world name, or {@code null} for global
     * @return a world context when the user exists in the backend, otherwise empty
     */
    public Optional<UserWorldContext> findInWorld(UUID uuid, String world) {
        return find(uuid).inWorld(world);
    }

    /**
     * Looks up a persisted user and returns a world-bound context when present.
     *
     * <p>Equivalent to {@code find(identifier).inWorld(world)}.</p>
     *
     * @param identifier user name or UUID string
     * @param world target world name, or {@code null} for global
     * @return a world context when the user exists in the backend, otherwise empty
     */
    public Optional<UserWorldContext> findInWorld(String identifier, String world) {
        return find(identifier).inWorld(world);
    }

    /**
     * Fluent terminal for a resolved (materializing) user lookup.
     *
     * <p>Obtained from {@link #resolve(UUID)} or {@link #resolve(String)}.</p>
     */
    public static final class ResolvedUser {
        private final UserRef ref;

        ResolvedUser(UserRef ref) {
            this.ref = ref;
        }

        /**
         * Returns the resolved {@link User}, creating a backend record when necessary.
         *
         * @return a live user handle (never {@code null})
         */
        public User get() {
            return ref.resolve();
        }

        /**
         * Returns a world-bound view of the resolved user.
         *
         * @param world target world name, or {@code null} for global
         * @return a {@link UserWorldContext} for the given world
         */
        public UserWorldContext inWorld(String world) {
            return ref.inWorld(world);
        }

        /**
         * Returns a global (all-worlds) view of the resolved user.
         *
         * @return a {@link UserWorldContext} for the global namespace
         */
        public UserWorldContext global() {
            return ref.inWorld(dev.rono.permissions.api.world.Worlds.GLOBAL);
        }
    }

    /**
     * Fluent terminal for an optional (non-materializing) user lookup.
     *
     * <p>Obtained from {@link #find(UUID)} or {@link #find(String)}.</p>
     */
    public static final class FoundUser {
        private final UserRef ref;

        FoundUser(UserRef ref) {
            this.ref = ref;
        }

        /**
         * Returns the persisted user, if any.
         *
         * @return the user when stored in the backend, otherwise empty
         */
        public Optional<User> get() {
            return ref.find();
        }

        /**
         * Returns a world-bound view of the user when persisted.
         *
         * @param world target world name, or {@code null} for global
         * @return a world context when the user exists, otherwise empty
         */
        public Optional<UserWorldContext> inWorld(String world) {
            return ref.findInWorld(world);
        }

        /**
         * Returns a global (all-worlds) view of the user when persisted.
         *
         * @return a world context when the user exists, otherwise empty
         */
        public Optional<UserWorldContext> global() {
            return ref.findInWorld(dev.rono.permissions.api.world.Worlds.GLOBAL);
        }
    }
}
