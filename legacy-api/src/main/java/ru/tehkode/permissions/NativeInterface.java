package ru.tehkode.permissions;

import java.util.Collection;
import java.util.UUID;
import ru.tehkode.permissions.events.PermissionEvent;

/**
 * Classic PermissionsEx host SPI (still named {@code NativeInterface} for 1.23.x-era compatibility).
 * Game servers implement the full definition including {@linkplain #callEvent(PermissionEvent)}; proxy
 * adapters may implement only {@link dev.rono.permissions.api.runtime.PlatformAdapter} instead.
 */
public interface NativeInterface {
    String UUIDToName(UUID uid);

    UUID nameToUUID(String name);

    boolean isOnline(UUID uuid);

    UUID getServerUUID();

    Collection<String> getWorldNames();

    /** Posts a legacy {@linkplain PermissionEvent} on the backing event bus where supported. */
    void callEvent(PermissionEvent event);

    String getOnlineWorldName(UUID uuid);

    String getOnlinePlayerName(UUID uuid);

    boolean isOperator(UUID uuid);
}
