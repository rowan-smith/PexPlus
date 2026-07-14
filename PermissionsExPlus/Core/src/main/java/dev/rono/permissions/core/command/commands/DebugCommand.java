package dev.rono.permissions.core.command.commands;

import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import dev.rono.permissions.core.command.AbstractCloudCommand;
import dev.rono.permissions.core.command.CoreCloudCommandContext;

@CommandMethod("pex debug")
public final class DebugCommand<C> extends AbstractCloudCommand<C> {

    public DebugCommand(final CoreCloudCommandContext<C> ctx) {
        super(ctx);
    }

    @CommandMethod("")
    @CommandPermission("pex.debug")
    public void debug(final C sender) {
        reply(sender, "§eUsage: /pex debug [on|off]");
    }

    @CommandMethod("on")
    @CommandPermission("pex.debug.on")
    public void on(final C sender) {
        reply(sender, "§aDebug mode enabled");
    }

    @CommandMethod("off")
    @CommandPermission("pex.debug.off")
    public void off(final C sender) {
        reply(sender, "§aDebug mode disabled");
    }
}
