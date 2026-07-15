package dev.rono.permissions.paper.platform;

import dev.rono.permissions.core.platform.PlatformConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public class PaperConfiguration implements PlatformConfiguration {

    private final JavaPlugin plugin;

    public PaperConfiguration(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Path dataDirectory() {
        return plugin.getDataPath();
    }

    @Override
    public void saveResource(String resource, boolean replace) {
        plugin.saveResource(resource, replace);
    }
}
