package dev.rono.permissions.api.query;

import dev.rono.permissions.api.service.PermissionServiceBridge;
import dev.rono.permissions.api.subject.Group;
import dev.rono.permissions.api.subject.GroupWorldContext;
import java.util.Optional;
import java.util.Set;

/**
 * Group registry — obtain via {@link PermissionQuery#groups()}.
 *
 * <h2>Resolve vs find</h2>
 * <ul>
 *   <li><strong>Resolve</strong> ({@link #resolve(String)}, {@link #inWorld(String, String)},
 *       {@link WorldScope#group(String)}): obtains a live {@link Group} or {@link GroupWorldContext},
 *       creating or materializing a backend record when the group does not yet exist. Safe for
 *       edits and membership changes.</li>
 *   <li><strong>Find</strong> ({@link #find(String)}, {@link #findInWorld(String, String)},
 *       {@link WorldScope#findGroup(String)}): queries persisted data only; returns
 *       {@link Optional#empty()} when no record is stored. Use for read-only checks where creating
 *       a group would be incorrect.</li>
 * </ul>
 *
 * <pre>{@code
 * int n = pex.query().groups().count();
 * pex.query().groups().resolve("vip").inWorld(world).members(true);
 * pex.query().groups().find("vip").get().ifPresent(g -> log.info(g.identifier()));
 * }</pre>
 */
public final class GroupsScope {

    private final PermissionServiceBridge service;

    GroupsScope(PermissionServiceBridge service) {
        this.service = service;
    }

    /**
     * Returns the number of group records in the active backend.
     *
     * @return persisted group count
     */
    public int count() {
        return service.groupCount();
    }

    /**
     * Returns every group name stored in the active backend.
     *
     * @return set of persisted group names
     */
    public Set<String> names() {
        return service.groupNames();
    }

    /**
     * Removes a group record from the active backend.
     *
     * @param name group name to delete
     */
    public void delete(String name) {
        service.deleteGroup(name);
    }

    /**
     * Begins a resolve chain for a group by name.
     *
     * <p>Materializes a backend record when none exists yet. Chain with {@link ResolvedGroup#inWorld(String)}
     * or {@link ResolvedGroup#global()} to obtain a world context.</p>
     *
     * @param name group name
     * @return a fluent handle for further resolution
     */
    public ResolvedGroup resolve(String name) {
        return new ResolvedGroup(SubjectRefs.group(service, name));
    }

    /**
     * Begins a find chain for a group by name.
     *
     * <p>Does not create records. Returns empty at each step when the group is not persisted.</p>
     *
     * @param name group name
     * @return a fluent handle for optional lookup
     */
    public FoundGroup find(String name) {
        return new FoundGroup(SubjectRefs.group(service, name));
    }

    /**
     * Resolves a group and returns a world-bound context in one step.
     *
     * <p>Equivalent to {@code resolve(name).inWorld(world)}.</p>
     *
     * @param name group name
     * @param world target world name, or {@code null} for global
     * @return a {@link GroupWorldContext} for the resolved group in the given world
     */
    public GroupWorldContext inWorld(String name, String world) {
        return resolve(name).inWorld(world);
    }

    /**
     * Looks up a persisted group and returns a world-bound context when present.
     *
     * <p>Equivalent to {@code find(name).inWorld(world)}.</p>
     *
     * @param name group name
     * @param world target world name, or {@code null} for global
     * @return a world context when the group exists in the backend, otherwise empty
     */
    public Optional<GroupWorldContext> findInWorld(String name, String world) {
        return find(name).inWorld(world);
    }

    /**
     * Fluent terminal for a resolved (materializing) group lookup.
     *
     * <p>Obtained from {@link #resolve(String)}.</p>
     */
    public static final class ResolvedGroup {
        private final GroupRef ref;

        ResolvedGroup(GroupRef ref) {
            this.ref = ref;
        }

        /**
         * Returns the resolved {@link Group}, creating a backend record when necessary.
         *
         * @return a live group handle (never {@code null})
         */
        public Group get() {
            return ref.resolve();
        }

        /**
         * Returns a world-bound view of the resolved group.
         *
         * @param world target world name, or {@code null} for global
         * @return a {@link GroupWorldContext} for the given world
         */
        public GroupWorldContext inWorld(String world) {
            return ref.inWorld(world);
        }

        /**
         * Returns a global (all-worlds) view of the resolved group.
         *
         * @return a {@link GroupWorldContext} for the global namespace
         */
        public GroupWorldContext global() {
            return ref.inWorld(dev.rono.permissions.api.world.Worlds.GLOBAL);
        }
    }

    /**
     * Fluent terminal for an optional (non-materializing) group lookup.
     *
     * <p>Obtained from {@link #find(String)}.</p>
     */
    public static final class FoundGroup {
        private final GroupRef ref;

        FoundGroup(GroupRef ref) {
            this.ref = ref;
        }

        /**
         * Returns the persisted group, if any.
         *
         * @return the group when stored in the backend, otherwise empty
         */
        public Optional<Group> get() {
            return ref.find();
        }

        /**
         * Returns a world-bound view of the group when persisted.
         *
         * @param world target world name, or {@code null} for global
         * @return a world context when the group exists, otherwise empty
         */
        public Optional<GroupWorldContext> inWorld(String world) {
            return ref.findInWorld(world);
        }

        /**
         * Returns a global (all-worlds) view of the group when persisted.
         *
         * @return a world context when the group exists, otherwise empty
         */
        public Optional<GroupWorldContext> global() {
            return ref.findInWorld(dev.rono.permissions.api.world.Worlds.GLOBAL);
        }
    }
}
