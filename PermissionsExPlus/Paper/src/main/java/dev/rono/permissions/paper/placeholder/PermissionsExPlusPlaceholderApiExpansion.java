package dev.rono.permissions.paper.placeholder;

import dev.rono.permissions.core.PexApiImpl;
import dev.rono.permissions.paper.PaperPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Optional PlaceholderAPI boundary; all resolution policy remains in Core.
 */
public final class PermissionsExPlusPlaceholderApiExpansion extends PlaceholderExpansion {
    private final PaperPlugin plugin;
    private final PexApiImpl<?> core;

    public PermissionsExPlusPlaceholderApiExpansion(PaperPlugin plugin, PexApiImpl<?> core) {
        this.plugin = plugin;
        this.core = core;
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getPluginMeta().getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getPluginMeta().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        if (!core.config().placeholderApiEnabled()) {
            return "";
        }

        return core.placeholders().resolve(player.getUniqueId(), params);
    }
}
