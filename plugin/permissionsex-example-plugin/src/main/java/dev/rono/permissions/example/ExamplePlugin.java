package dev.rono.permissions.example;

import dev.rono.permissions.api.PermissionsExApi;
import dev.rono.permissions.bukkit.PexBukkitPermissions;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;

/** Sample plugin using {@link PermissionsEx#getApi()}. */
public class ExamplePlugin extends JavaPlugin implements Listener {

    private PermissionsExApi permissions;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        if (!PermissionsEx.isAvailable()) {
            getLogger().warning("PermissionsEx is not available — is PermissionsEx loaded?");
            return;
        }

        permissions = PermissionsEx.getApi();
        var legacy = permissions.getLegacyPermissionManager();
        getLogger().info(String.format(Locale.ROOT,
                "PEX users=%d groups=%d",
                legacy.getUserIdentifiers().size(),
                legacy.getGroupNames().size()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        if (permissions == null) {
            return;
        }

        var player = event.getPlayer();
        var allowed = PexBukkitPermissions.on(player).hasPermission("my.node");
        var worldContext = PexBukkitPermissions.on(player).context();
        var displayName = worldContext.option("name");

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
