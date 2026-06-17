package dev.rono.permissions.paper;

import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import org.bukkit.command.CommandSender;
import ru.tehkode.permissions.spigot.bukkit.SpigotPermissionsExPlugin;
import ru.tehkode.permissions.spigot.bukkit.StrippingPaperCommandManager;

import java.util.function.Function;

/**
 * Paper runtime entry point. Uses {@link cloud.commandframework.paper.PaperCommandManager} for
 * Paper-native Brigadier integration.
 */
public final class PaperPermissionsExPlugin extends SpigotPermissionsExPlugin {

	@Override
	protected BukkitCommandManager<CommandSender> createCloudCommandManager() throws Exception {
		return new StrippingPaperCommandManager<>(
				this,
				CommandExecutionCoordinator.simpleCoordinator(),
				Function.identity(),
				Function.identity());
	}

	@Override
	protected void configureCloudBrigadier(BukkitCommandManager<CommandSender> manager) {
		if (!(manager instanceof PaperCommandManager<?> paperManager)) {
			super.configureCloudBrigadier(manager);
			return;
		}
		try {
			paperManager.registerBrigadier();
		} catch (BukkitCommandManager.BrigadierFailureException ex) {
			getLogger().fine("Paper Brigadier hook not available: " + ex.getMessage());
		}
	}
}
