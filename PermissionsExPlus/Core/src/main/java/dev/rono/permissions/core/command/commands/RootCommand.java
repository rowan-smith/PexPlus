package dev.rono.permissions.core.command.commands;

import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import dev.rono.permissions.core.command.AbstractCloudCommand;
import dev.rono.permissions.core.command.CoreCloudCommandContext;

@CommandMethod("pex")
public final class RootCommand<C> extends AbstractCloudCommand<C> {

    public RootCommand(final CoreCloudCommandContext<C> ctx) {
        super(ctx);
    }

    @CommandMethod("")
    @CommandPermission("pex.admin")
    public void root(final C sender) {
        reply(sender, "§6PermissionsExPlus Commands:");
        reply(sender, "§e/pex user <user> §7- User management");
        reply(sender, "§e/pex group <group> §7- Group management");
        reply(sender, "§e/pex ladder [ladder] §7- Ladder management");
        reply(sender, "§e/pex backend §7- Storage management");
        reply(sender, "§e/pex reload §7- Reload configuration");
        reply(sender, "§e/pex version §7- Show version");
        reply(sender, "§e/pex debug [on|off] §7- Toggle debug");
    }

    @CommandMethod("help")
    @CommandPermission("pex.admin")
    public void help(final C sender) {
        root(sender);
    }
}
