package dev.rono.permissions.example;

import dev.rono.permissions.api.service.PexPermissionService;
import dev.rono.permissions.bukkit.PexBukkitPermissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

/** Sample plugin using the modern {@link PexPermissionService} API. */
public class ExamplePlugin extends JavaPlugin implements Listener {

    private PexPermissionService permissions;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        RegisteredServiceProvider<PexPermissionService> registration =
                getServer().getServicesManager().getRegistration(PexPermissionService.class);
        if (registration == null) {
            getLogger().warning("PexPermissionService is not registered — is PermissionsEx loaded?");
            return;
        }

        permissions = registration.getProvider();
        getLogger().info(String.format(Locale.ROOT,
                "PEX backend: %s (%s), users=%d groups=%d worlds=%d",
                permissions.backend().type(),
                permissions.backend().simpleName(),
                permissions.users().count(),
                permissions.groups().count(),
                permissions.worlds().count()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        if (permissions == null) {
            return;
        }

        Player player = event.getPlayer();
        boolean allowed = PexBukkitPermissions.on(player).hasPermission("my.node");
        var worldContext = PexBukkitPermissions.on(player).context();
        String displayName = worldContext.option("name");
        if (displayName == null) {
            displayName = player.getName();
        }
        final String resolvedName = displayName;

        getLogger().fine(() -> String.format(Locale.ROOT,
                "pex uuid=%s user=%s allowed(my.node)=%s groups=%s directPerms=%s timed=%s",
                player.getUniqueId(),
                resolvedName,
                allowed,
                worldContext.groups(),
                worldContext.permissions(),
                worldContext.timedPermissionEntries()));
    }
}
