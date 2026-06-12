package dev.rono.permissions.bukkit;

import org.bukkit.entity.Player;

/**
 * Bukkit {@link Player} convenience helpers for PermissionsEx.
 *
 * <pre>{@code
 * BukkitPermissions.on(player).has("my.node");
 * BukkitPermissions.on(player).context().inGroup("vip");
 * }</pre>
 */
public final class BukkitPermissions {
    private BukkitPermissions() {}

    /** Player-scoped entry (resolves {@code PermissionService} from {@code ServicesManager}). */
    public static PlayerScope on(Player player) {
        return new PlayerScope(PexServices.require(), player);
    }
}
