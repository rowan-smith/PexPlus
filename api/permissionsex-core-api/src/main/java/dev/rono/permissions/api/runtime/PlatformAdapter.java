package dev.rono.permissions.api.runtime;

import dev.rono.permissions.api.bus.PermissionDispatch;
import java.util.Collection;
import java.util.UUID;

/**
 * Host integration bridge (Spigot, Bungee, tests). No Bukkit types on this interface.
 *
 * <p>Implemented by platform modules to supply player identity, realm names, and event publication.</p>
 */
public interface PlatformAdapter {

    /**
     * Resolves a UUID to the current display/login name when known.
     *
     * @param uid player UUID
     * @return name, or {@code null} if unknown
     */
    String uuidToName(UUID uid);

    /**
     * Resolves a name to a UUID when the platform knows the player.
     *
     * @param name player name
     * @return UUID, or {@code null} if unknown
     */
    UUID nameToUuid(String name);

    /**
     * Reports whether the player is currently online on this host.
     *
     * @param uuid player UUID
     * @return {@code true} if online
     */
    boolean isOnline(UUID uuid);

    /**
     * Returns a stable identifier for this server instance.
     *
     * @return server UUID
     */
    UUID serverId();

    /**
     * Returns realm names known to this host.
     *
     * <p>Worlds on game servers; backend server ids on proxies.</p>
     *
     * @return registered realm names
     */
    Collection<String> realmNames();

    /**
     * Publishes a permission dispatch to the modern event bus and platform-specific listeners.
     *
     * @param dispatch entity or system dispatch payload
     */
    void publish(PermissionDispatch dispatch);

    /**
     * Returns the current realm/world when the player is online.
     *
     * @param player player UUID
     * @return realm name, or {@code null} when offline or unknown
     */
    String onlineRealm(UUID player);

    /**
     * Returns the display name when the player is online.
     *
     * @param player player UUID
     * @return display name, or {@code null} when offline or unknown
     */
    String onlineDisplayName(UUID player);

    /**
     * Reports whether the player is a server operator on this host.
     *
     * @param player player UUID
     * @return {@code true} if op
     */
    boolean isOperator(UUID player);
}
