package dev.rono.permissions.api.query;

import dev.rono.permissions.api.PermissionsExException;
import dev.rono.permissions.api.event.PermissionEventBus;
import dev.rono.permissions.api.service.PermissionService;
import dev.rono.permissions.api.service.PermissionServiceBridge;
import dev.rono.permissions.api.session.PermissionEditSession;
import dev.rono.permissions.api.world.Worlds;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Single fluent entry for the modern PermissionsEx API.
 *
 * <p>Obtain via {@link PermissionService#query()}. Most operations delegate to scoped helpers
 * ({@link WorldScope}, {@link UsersScope}, {@link GroupsScope}, {@link BackendScope}).</p>
 *
 * <pre>{@code
 * pex.query().world(player.getWorld().getName()).user(player.getUniqueId()).inGroup("vip", true);
 * pex.query().users().count();
 * pex.query().backend().activate("file");
 * }</pre>
 */
public final class PermissionQuery {

    private final PermissionServiceBridge service;

    private PermissionQuery(PermissionService service) {
        this.service = PermissionService.requireBridge(service);
    }

    /**
     * Creates a query bound to the given service instance.
     *
     * @param service registered {@link PermissionService} (must implement {@link PermissionServiceBridge})
     * @return a new {@link PermissionQuery}
     * @throws IllegalStateException if {@code service} does not implement {@link PermissionServiceBridge}
     */
    public static PermissionQuery of(PermissionService service) {
        return new PermissionQuery(service);
    }

    /**
     * Begins a world-scoped fluent chain for permission checks and subject edits.
     *
     * <p>Recommended entry point when the target realm is known. Use {@link #global()} for the
     * all-worlds namespace ({@link Worlds#GLOBAL}).</p>
     *
     * @param world world name, or {@code null}/{@code ""} for global
     * @return a {@link WorldScope} bound to the normalized world
     */
    public WorldScope world(String world) {
        return new WorldScope(service, world);
    }

    /**
     * Begins a fluent chain in the global (all-worlds) namespace.
     *
     * <p>Equivalent to {@code world(Worlds.GLOBAL)}.</p>
     *
     * @return a {@link WorldScope} for the global namespace
     */
    public WorldScope global() {
        return world(Worlds.GLOBAL);
    }

    /**
     * Returns a world scope only when the name is registered on the platform.
     *
     * <p>Unlike {@link #world(String)}, this does not construct a scope for unknown world names.</p>
     *
     * @param world world name to look up
     * @return a {@link WorldScope} when {@code world} is registered, otherwise empty
     */
    public Optional<WorldScope> findWorld(String world) {
        return service.worlds().contains(world) ? Optional.of(world(world)) : Optional.empty();
    }

    /**
     * Accesses the user registry: counts, identifiers, resolve/find subjects, and deletion.
     *
     * @return a {@link UsersScope} for global (non-world-preset) user operations
     */
    public UsersScope users() {
        return new UsersScope(service);
    }

    /**
     * Accesses the group registry: counts, names, resolve/find subjects, and deletion.
     *
     * @return a {@link GroupsScope} for global (non-world-preset) group operations
     */
    public GroupsScope groups() {
        return new GroupsScope(service);
    }

    /**
     * Accesses backend introspection, activation, and import/export administration.
     *
     * @return a {@link BackendScope} for the active backend
     */
    public BackendScope backend() {
        return new BackendScope(service);
    }

    /**
     * Returns the permission-domain event bus.
     *
     * @return the shared {@link PermissionEventBus}
     */
    public PermissionEventBus events() {
        return service.events();
    }

    /**
     * Returns world names registered on the platform.
     *
     * @return collection of known world names
     */
    public Collection<String> worlds() {
        return service.worlds();
    }

    /**
     * Reports whether PermissionsEx debug logging is enabled.
     *
     * @return {@code true} when debug mode is active
     */
    public boolean isDebug() {
        return service.isDebug();
    }

    /**
     * Reloads permission data from the active backend synchronously.
     *
     * @throws PermissionsExException if reload fails
     */
    public void reload() throws PermissionsExException {
        service.reload();
    }

    /**
     * Reloads permission data from the active backend asynchronously.
     *
     * @return a future that completes when reload finishes
     */
    public CompletableFuture<Void> reloadAsync() {
        return service.reloadAsync();
    }

    /**
     * Opens a batch edit session that defers persistence until {@link PermissionEditSession#save()}.
     *
     * @return a new {@link PermissionEditSession}
     */
    public PermissionEditSession editSession() {
        return service.openEditSession();
    }
}
