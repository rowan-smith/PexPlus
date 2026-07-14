package dev.rono.permissions.core.command.commands;

import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import dev.rono.permissions.core.command.AbstractCloudCommand;
import dev.rono.permissions.core.command.CoreCloudCommandContext;

@CommandMethod("pex backend")
public final class BackendCommand<C> extends AbstractCloudCommand<C> {

    public BackendCommand(final CoreCloudCommandContext<C> ctx) {
        super(ctx);
    }

    @CommandMethod("")
    @CommandPermission("pex.backend")
    public void backend(final C sender) {
        reply(sender, "§eUsage: /pex backend [info|list|switch]");
    }

    @CommandMethod("info")
    @CommandPermission("pex.backend.info")
    public void info(final C sender) {
        reply(sender, "§6Storage Backend:");
        reply(sender, "§7  Type: §fMemoryStorageEngine");
        reply(sender, "§7  Status: §aOnline");
    }

    @CommandMethod("list")
    @CommandPermission("pex.backend.list")
    public void list(final C sender) {
        reply(sender, "§6Available Backends:");
        reply(sender, "§7  - §fMemoryStorageEngine");
    }


}
