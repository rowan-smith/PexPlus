package dev.rono.permissions.test;

import dev.rono.permissions.api.PexProvider;
import dev.rono.permissions.api.platform.context.ContextRegistration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Test extends JavaPlugin implements Listener {
    private final ConcurrentMap<UUID, Boolean> flying = new ConcurrentHashMap<>();

    private ContextRegistration valueRegistration;

    private ContextRegistration calculatorRegistration;

    @Override
    public void onEnable() {
        var api = PexProvider.get();

        var contexts = api.contexts();

        valueRegistration = contexts.registry().registerContextType("flying", () -> List.of("false", "true"));

        calculatorRegistration = contexts.registerCalculator((uuid, consumer) -> {
            var active = flying.get(uuid);

            if (active != null) {
                consumer.accept("flying", Boolean.toString(active));
            }
        });

        getServer().getPluginManager().registerEvents(this, this);

        getServer().getOnlinePlayers().forEach(this::track);
    }

    @Override
    public void onDisable() {
        if (calculatorRegistration != null) {
            calculatorRegistration.close();
        }

        if (valueRegistration != null) {
            valueRegistration.close();
        }

        flying.clear();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFlightToggle(PlayerToggleFlightEvent event) {
        var player = event.getPlayer();

        flying.put(player.getUniqueId(), event.isFlying());

        player.recalculatePermissions();
        player.updateCommands();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        track(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        unTrack(event.getPlayer().getUniqueId());
    }

    private void track(Player player) {
        flying.put(player.getUniqueId(), player.isFlying());
    }

    private void unTrack(UUID playerId) {
        flying.remove(playerId);
    }
}
