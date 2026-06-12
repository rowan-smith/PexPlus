package ru.tehkode.permissions.bukkit;

import dev.rono.permissions.api.PermissionsExApi;
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
 * <p>The live {@code JavaPlugin} implementation ships in {@code permissionsex-spigot} and is
 * registered via {@code plugin.yml}. Hook plugins depend on this class for stable static accessors
 * without linking against the full plugin module.</p>
 *
 * <pre>{@code
 * PermissionsExApi api = PermissionsEx.getApi();
 * User user = api.getUserManager().getUser(uuid);
 * }</pre>
 */
public final class PermissionsEx {
    private PermissionsEx() {}

    /**
     * Returns the PermissionsEx Bukkit plugin instance, if loaded.
     *
     * @return the plugin, or {@code null} if PermissionsEx is not installed
     */
    public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("PermissionsEx");
    }

    /**
     * Returns whether PermissionsEx is loaded, enabled, and exposing services.
     *
     * @return {@code true} if the plugin and its service providers are available
     */
    public static boolean isAvailable() {
        Plugin plugin = getPlugin();
        if (plugin == null || !plugin.isEnabled()) {
            return false;
        }

        var modern = Bukkit.getServer().getServicesManager().getRegistration(PermissionsExApi.class);
        if (modern != null && modern.getProvider() != null) {
            return true;
        }

        var legacy = Bukkit.getServer().getServicesManager().getRegistration(PermissionManager.class);
        return legacy != null && legacy.getProvider() != null;
    }

    /**
     * Returns the modern PermissionsEx hook API.
     *
     * @return active API facade; never {@code null}
     * @throws PermissionsNotAvailable if PermissionsEx is not loaded or the API is not registered
     */
    public static PermissionsExApi getApi() {
        if (!isAvailable()) {
            throw new PermissionsNotAvailable();
        }

        var reg = Bukkit.getServer().getServicesManager().getRegistration(PermissionsExApi.class);
        if (reg != null && reg.getProvider() != null) {
            return reg.getProvider();
        }

        var legacyReg = Bukkit.getServer().getServicesManager().getRegistration(PermissionManager.class);
        if (legacyReg != null && legacyReg.getProvider() instanceof PermissionsExApi api) {
            return api;
        }

        throw new PermissionsNotAvailable();
    }

    /**
     * Returns the registered legacy {@link PermissionManager} service.
     *
     * @return active permission manager; never {@code null}
     * @throws PermissionsNotAvailable if PermissionsEx is not loaded or the manager is not registered
     * @deprecated Use {@link #getApi()} and {@link PermissionsExApi#getLegacyPermissionManager()} for classic integrations.
     */
    @Deprecated(forRemoval = false)
    public static PermissionManager getPermissionManager() {
        return getApi().getLegacyPermissionManager();
    }

    /**
     * Returns the permission user associated with an online player.
     *
     * @param player online player; must not be {@code null}
     * @return permission user handle; never {@code null} when PermissionsEx is available
     * @throws PermissionsNotAvailable if PermissionsEx is not loaded or the manager is not registered
     */
    @Deprecated(forRemoval = false)
    public static PermissionUser getUser(Player player) {
        return getPermissionManager().getUser(player);
    }

    /**
     * Returns the permission user identified by name.
     *
     * @param name player or user name
     * @return permission user handle; never {@code null} when PermissionsEx is available
     * @throws PermissionsNotAvailable if PermissionsEx is not loaded or the manager is not registered
     */
    @Deprecated(forRemoval = false)
    public static PermissionUser getUser(String name) {
        return getPermissionManager().getUser(name);
    }
}
