package dev.rono.permissions.core.command.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.realm.BuiltinRealm;
import dev.rono.permissions.api.user.User;
import dev.rono.permissions.core.command.AbstractCloudCommand;
import dev.rono.permissions.core.command.CoreCloudCommandContext;

@CommandMethod("pex user <user>")
public final class UserCommand<C> extends AbstractCloudCommand<C> {

    public UserCommand(final CoreCloudCommandContext<C> ctx) {
        super(ctx);
    }

    @CommandMethod("")
    @CommandPermission("pex.user")
    public void info(final C sender, final @Argument("user") User user) {
        reply(sender, "§6User: §e" + user.name());
        reply(sender, "§7  UUID: §f" + user.id());
    }

    @CommandMethod("permissions list")
    @CommandPermission("pex.user.permissions.list")
    public void permissionsList(final C sender, final @Argument("user") User user) {
        reply(sender, "§6Permissions for §e" + user.name() + "§6:");
        reply(sender, "§7  (permission listing not yet available)");
    }

    @CommandMethod("permissions add <permission>")
    @CommandPermission("pex.user.permissions.add")
    public void permissionsAdd(
            final C sender,
            final @Argument("user") User user,
            final @Argument(value = "permission", suggestions = "pex-permission") String permission
    ) {
        ctx.api().users().addPermission(user, permission);
        reply(sender, "§aAdded permission §f" + permission + " §ato §e" + user.name());
    }

    @CommandMethod("permissions remove <permission>")
    @CommandPermission("pex.user.permissions.remove")
    public void permissionsRemove(
            final C sender,
            final @Argument("user") User user,
            final @Argument(value = "permission", suggestions = "pex-permission") String permission
    ) {
        ctx.api().users().removePermission(user, permission);
        reply(sender, "§aRemoved permission §f" + permission + " §afrom §e" + user.name());
    }

    @CommandMethod("permissions check <permission>")
    @CommandPermission("pex.user.permissions.check")
    public void permissionsCheck(
            final C sender,
            final @Argument("user") User user,
            final @Argument(value = "permission", suggestions = "pex-permission") String permission
    ) {
        final var result = ctx.api().permissions().check(user, permission, user.context(BuiltinRealm.GLOBAL));
        reply(sender, "§6Permission check for §e" + user.name() + "§6:");
        reply(sender, "§7  " + permission + " §f\u2192 " + result);
    }

    @CommandMethod("permissions trace <permission>")
    @CommandPermission("pex.user.permissions.trace")
    public void permissionsTrace(
            final C sender,
            final @Argument("user") User user,
            final @Argument(value = "permission", suggestions = "pex-permission") String permission
    ) {
        final var result = ctx.api().permissions().check(user, permission, user.context(BuiltinRealm.GLOBAL));
        reply(sender, "§6Permission trace for §e" + user.name() + "§6:");
        reply(sender, "§7  " + permission + " §f\u2192 " + result);
    }

    @CommandMethod("groups list")
    @CommandPermission("pex.user.groups.list")
    public void groupsList(final C sender, final @Argument("user") User user) {
        reply(sender, "§6Groups for §e" + user.name() + "§6:");
        reply(sender, "§7  (group listing not yet available)");
    }

    @CommandMethod("groups add <group>")
    @CommandPermission("pex.user.groups.add")
    public void groupsAdd(
            final C sender,
            final @Argument("user") User user,
            final @Argument(value = "group", suggestions = "pex-group") Group group
    ) {
        reply(sender, "§aAdded to group: §f" + group.name());
    }

    @CommandMethod("groups remove <group>")
    @CommandPermission("pex.user.groups.remove")
    public void groupsRemove(
            final C sender,
            final @Argument("user") User user,
            final @Argument(value = "group", suggestions = "pex-group") Group group
    ) {
        reply(sender, "§aRemoved from group: §f" + group.name());
    }

    @CommandMethod("groups set <group>")
    @CommandPermission("pex.user.groups.set")
    public void groupsSet(
            final C sender,
            final @Argument("user") User user,
            final @Argument(value = "group", suggestions = "pex-group") Group group
    ) {
        reply(sender, "§aSet group for §e" + user.name() + " §ato §f" + group.name());
    }

    @CommandMethod("options list")
    @CommandPermission("pex.user.options.list")
    public void optionsList(final C sender, final @Argument("user") User user) {
        reply(sender, "§6Options for §e" + user.name() + "§6:");
        reply(sender, "§7  (options not yet available)");
    }

    @CommandMethod("options get <key>")
    @CommandPermission("pex.user.options.get")
    public void optionsGet(final C sender, final @Argument("user") User user, final @Argument("key") String key) {
        reply(sender, "§6Option §e" + key + " §6for §e" + user.name() + "§6:");
        reply(sender, "§7  (options not yet available)");
    }

    @CommandMethod("options set <key> <value>")
    @CommandPermission("pex.user.options.set")
    public void optionsSet(
            final C sender,
            final @Argument("user") User user,
            final @Argument("key") String key,
            final @Argument("value") String value
    ) {
        reply(sender, "§aSet option §e" + key + " §ato §f" + value + " §afor §e" + user.name());
    }

    @CommandMethod("options unset <key>")
    @CommandPermission("pex.user.options.unset")
    public void optionsUnset(final C sender, final @Argument("user") User user, final @Argument("key") String key) {
        reply(sender, "§aUnset option §e" + key + " §afor §e" + user.name());
    }

    @CommandMethod("delete")
    @CommandPermission("pex.user.delete")
    public void delete(final C sender, final @Argument("user") User user) {
        reply(sender, "§eUser deletion not yet implemented");
    }
}
