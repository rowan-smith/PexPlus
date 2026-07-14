package dev.rono.permissions.spigot;

import dev.rono.permissions.api.PermissionsExPlusProvider;
import dev.rono.permissions.core.PermissionsExPlusCore;
import dev.rono.permissions.spigot.platform.SpigotPlatform;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotPlugin extends JavaPlugin {

    private PermissionsExPlusCore<CommandSender> core;

    @Override
    public void onLoad() {
        core = new PermissionsExPlusCore<>(new SpigotPlatform(this));
    }

    @Override
    public void onEnable() {
        core.start();

        PermissionsExPlusProvider.set(core);
    }

    @Override
    public void onDisable() {
        core.stop();
    }
}
