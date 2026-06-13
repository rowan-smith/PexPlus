package dev.rono.permissions.api.subject;

/**
 * Server-scoped projection of a {@link PermissionSubject}.
 *
 * <p>Obtained via {@link PermissionSubject#inServer(String)}. Every method on this context applies to
 * the bound server returned by {@link #server()} — callers do not pass a server argument again.
 * {@link dev.rono.permissions.api.world.Worlds#GLOBAL} selects the global namespace.</p>
 *
 * <p>On proxy runtimes (BungeeCord/Waterfall, Velocity), a <em>server</em> is a backend id from
 * {@link dev.rono.permissions.api.runtime.PlatformAdapter#realmNames()}. There is no separate Minecraft
 * dimension on the proxy; server names are stored as permission namespaces (the same {@code String world}
 * key used everywhere in the engine). {@link PermissionSubject#inWorld(String)} and
 * {@link PermissionSubject#inServer(String)} bind the same namespace on proxies — prefer {@code inServer}
 * for clarity in proxy plugins.</p>
 *
 * <p>On game servers, {@code inServer} still binds a realm namespace by name (for example a linked
 * backend id in cross-network setups). Prefer {@link SubjectWorldContext} / {@link PermissionSubject#inWorld(String)}
 * when scoping to a loaded Bukkit/Spigot world.</p>
 *
 * <p><strong>Thin facade invariant:</strong> same as {@link SubjectWorldContext} — implementations delegate
 * to the underlying subject with the bound realm fixed; no duplicated resolution or persistence logic.</p>
 *
 * @see SubjectWorldContext
 */
public interface SubjectServerContext extends SubjectWorldContext {

    /**
     * Returns the server this context is bound to.
     *
     * <p>Equivalent to {@link #world()} — the storage namespace for permissions on this context.</p>
     *
     * @return {@link dev.rono.permissions.api.world.Worlds#GLOBAL} or a specific server/realm name
     */
    default String server() {
        return world();
    }
}
