package dev.rono.permissions.core.command.commands;

import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import dev.rono.permissions.core.command.AbstractCloudCommand;
import dev.rono.permissions.core.command.CoreCloudCommandContext;

@CommandMethod("pex version")
public final class VersionCommand<C> extends AbstractCloudCommand<C> {

    public VersionCommand(final CoreCloudCommandContext<C> ctx) {
        super(ctx);
    }

    @CommandMethod("")
    @CommandPermission("pex.version")
    public void version(final C sender) {
        final var pkg = getClass().getPackage();
        final var ver = pkg != null ? pkg.getImplementationVersion() : "1.0.0-SNAPSHOT";
        reply(sender, "§6PermissionsExPlus §ev" + (ver != null ? ver : "1.0.0-SNAPSHOT"));
    }
}
