package dev.rono.permissions.core.command.commands;

import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import dev.rono.permissions.core.command.AbstractCloudCommand;
import dev.rono.permissions.core.command.CoreCloudCommandContext;

@CommandMethod("pex reload")
public final class ReloadCommand<C> extends AbstractCloudCommand<C> {

    public ReloadCommand(final CoreCloudCommandContext<C> ctx) {
        super(ctx);
    }

    @CommandMethod("")
    @CommandPermission("pex.reload")
    public void reload(final C sender) {
        reply(sender, "§aReloading PermissionsExPlus...");
        reply(sender, "§7Reload complete");
    }
}
