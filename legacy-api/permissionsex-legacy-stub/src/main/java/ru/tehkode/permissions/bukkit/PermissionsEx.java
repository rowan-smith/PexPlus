package ru.tehkode.permissions.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.exceptions.PermissionsNotAvailable;

/**
 * Compile-only static entry points for hook plugins.
 *
 * <pre>{@code
 * PermissionManager api = PermissionsEx.getApi();
 * User user = api.getUserManager().getUser(uuid);
 * }</pre>
 */
public final class PermissionsEx {
    private PermissionsEx() {}

    public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("PermissionsEx");
    }

    public static boolean isAvailable() {
        Plugin plugin = getPlugin();
        if (plugin == null || !plugin.isEnabled()) {
            return false;
        }
        RegisteredServiceProvider<PermissionManager> reg =
                Bukkit.getServer().getServicesManager().getRegistration(PermissionManager.class);
        return reg != null && reg.getProvider() != null;
    }

    /**
     * Returns the PermissionsEx hook API ({@link PermissionManager} with modern manager accessors).
     */
    public static PermissionManager getApi() {
        if (!isAvailable()) {
            throw new PermissionsNotAvailable();
        }
        RegisteredServiceProvider<PermissionManager> reg =
                Bukkit.getServer().getServicesManager().getRegistration(PermissionManager.class);
        return reg.getProvider();
    }

    /**
     * @deprecated Use {@link #getApi()}.
     */
    @Deprecated(forRemoval = false)
    public static PermissionManager getPermissionManager() {
        return getApi();
    }

    /**
     * @deprecated Use {@link PermissionManager#getUserManager()} and modern {@code User} types.
     */
    @Deprecated(forRemoval = false)
    public static PermissionUser getUser(Player player) {
        return getApi().getUser(player);
    }

    /**
     * @deprecated Use {@link PermissionManager#getUserManager()} and modern {@code User} types.
     */
    @Deprecated(forRemoval = false)
    public static PermissionUser getUser(String name) {
        return getApi().getUser(name);
    }
}
