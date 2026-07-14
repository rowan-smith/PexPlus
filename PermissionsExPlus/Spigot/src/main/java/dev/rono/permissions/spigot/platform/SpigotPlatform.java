package dev.rono.permissions.spigot.platform;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import dev.rono.permissions.core.bridge.Platform;
import dev.rono.permissions.core.bridge.PlatformConfiguration;
import dev.rono.permissions.core.bridge.PlatformLogger;
import dev.rono.permissions.core.bridge.PlatformScheduler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotPlatform implements Platform<CommandSender> {

    private final JavaPlugin plugin;

    private final SpigotLogger logger;
    private final SpigotScheduler scheduler;
    private final SpigotConfiguration configuration;

    public SpigotPlatform(JavaPlugin plugin) {
        this.plugin = plugin;

        this.logger = new SpigotLogger(plugin.getLogger());
        this.scheduler = new SpigotScheduler(plugin);
        this.configuration = new SpigotConfiguration(plugin);
    }

    @Override
    public PlatformLogger logger() {
        return logger;
    }

    @Override
    public PlatformScheduler scheduler() {
        return scheduler;
    }

    @Override
    public PlatformConfiguration configuration() {
        return configuration;
    }

    @Override
    public Class<CommandSender> senderType() {
        return CommandSender.class;
    }

    @Override
    public void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    @Override
    public CommandManager<CommandSender> createCommandManager() throws Exception {
        var manager = BukkitCommandManager.createNative(plugin, CommandExecutionCoordinator.simpleCoordinator());

        if (manager.queryCapabilities().contains(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            manager.registerBrigadier();
        }

        return manager;
    }
}
