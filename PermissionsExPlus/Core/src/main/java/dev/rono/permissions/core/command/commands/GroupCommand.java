package dev.rono.permissions.core.command.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.realm.BuiltinRealm;
import dev.rono.permissions.api.user.User;
import dev.rono.permissions.core.command.AbstractCloudCommand;
import dev.rono.permissions.core.command.CoreCloudCommandContext;

@CommandMethod("pex group <group>")
public final class GroupCommand<C> extends AbstractCloudCommand<C> {

    public GroupCommand(final CoreCloudCommandContext<C> ctx) {
        super(ctx);
    }

    @CommandMethod("create")
    @CommandPermission("pex.group.create")
    public void create(final C sender, final @Argument("group") String name) {
        ctx.api().groups().create(name);
        reply(sender, "§aCreated group §e" + name);
    }

    @CommandMethod("delete")
    @CommandPermission("pex.group.delete")
    public void delete(final C sender, final @Argument("group") Group group) {
        ctx.api().groups().delete(group.name());
        reply(sender, "§aDeleted group §e" + group.name());
    }

    @CommandMethod("permissions list")
    @CommandPermission("pex.group.permissions.list")
    public void permissionsList(final C sender, final @Argument("group") Group group) {
        reply(sender, "§6Permissions for §e" + group.name() + "§6:");
        for (final var perm : group.permissions()) {
            reply(sender, "§7  - §f" + perm.permission() + " §7(" + perm.value() + ")");
        }
    }

    @CommandMethod("permissions add <permission>")
    @CommandPermission("pex.group.permissions.add")
    public void permissionsAdd(
            final C sender,
            final @Argument("group") Group group,
            final @Argument(value = "permission", suggestions = "pex-permission") String permission
    ) {
        ctx.api().groups().addPermission(group, permission);
        reply(sender, "§aAdded permission §f" + permission + " §ato §e" + group.name());
    }

    @CommandMethod("permissions remove <permission>")
    @CommandPermission("pex.group.permissions.remove")
    public void permissionsRemove(
            final C sender,
            final @Argument("group") Group group,
            final @Argument(value = "permission", suggestions = "pex-permission") String permission
    ) {
        ctx.api().groups().removePermission(group, permission);
        reply(sender, "§aRemoved permission §f" + permission + " §afrom §e" + group.name());
    }

    @CommandMethod("permissions check <permission>")
    @CommandPermission("pex.group.permissions.check")
    public void permissionsCheck(
            final C sender,
            final @Argument("group") Group group,
            final @Argument(value = "permission", suggestions = "pex-permission") String permission
    ) {
        final var result = ctx.api().permissions().check(group, permission, group.context(BuiltinRealm.GLOBAL));
        reply(sender, "§6Permission check for §e" + group.name() + "§6:");
        reply(sender, "§7  " + permission + " §f\u2192 " + result);
    }

    @CommandMethod("permissions trace <permission>")
    @CommandPermission("pex.group.permissions.trace")
    public void permissionsTrace(
            final C sender,
            final @Argument("group") Group group,
            final @Argument(value = "permission", suggestions = "pex-permission") String permission
    ) {
        final var result = ctx.api().permissions().check(group, permission, group.context(BuiltinRealm.GLOBAL));
        reply(sender, "§6Permission trace for §e" + group.name() + "§6:");
        reply(sender, "§7  " + permission + " §f\u2192 " + result);
    }

    @CommandMethod("parents list")
    @CommandPermission("pex.group.parents.list")
    public void parentsList(final C sender, final @Argument("group") Group group) {
        reply(sender, "§6Parents for §e" + group.name() + "§6:");
        for (final var parent : group.parents()) {
            reply(sender, "§7  - §f" + parent);
        }
    }

    @CommandMethod("parents add <parent>")
    @CommandPermission("pex.group.parents.add")
    public void parentsAdd(
            final C sender,
            final @Argument("group") Group group,
            final @Argument(value = "parent", suggestions = "pex-group") Group parent
    ) {
        ctx.api().groups().addParent(group, parent);
        reply(sender, "§aAdded parent §f" + parent.name() + " §ato §e" + group.name());
    }

    @CommandMethod("parents remove <parent>")
    @CommandPermission("pex.group.parents.remove")
    public void parentsRemove(
            final C sender,
            final @Argument("group") Group group,
            final @Argument(value = "parent", suggestions = "pex-group") Group parent
    ) {
        ctx.api().groups().removeParent(group, parent);
        reply(sender, "§aRemoved parent §f" + parent.name() + " §afrom §e" + group.name());
    }

    @CommandMethod("members list")
    @CommandPermission("pex.group.members.list")
    public void membersList(final C sender, final @Argument("group") Group group) {
        reply(sender, "§6Members of §e" + group.name() + "§6:");
        reply(sender, "§7  (member listing not yet available)");
    }

    @CommandMethod("members add <user>")
    @CommandPermission("pex.group.members.add")
    public void membersAdd(
            final C sender,
            final @Argument("group") Group group,
            final @Argument(value = "user", suggestions = "pex-user") User user
    ) {
        reply(sender, "§aAdded §e" + user.name() + " §ato group §e" + group.name());
    }

    @CommandMethod("members remove <user>")
    @CommandPermission("pex.group.members.remove")
    public void membersRemove(
            final C sender,
            final @Argument("group") Group group,
            final @Argument(value = "user", suggestions = "pex-user") User user
    ) {
        reply(sender, "§aRemoved §e" + user.name() + " §afrom group §e" + group.name());
    }

    @CommandMethod("options set <key> <value>")
    @CommandPermission("pex.group.options.set")
    public void optionsSet(
            final C sender,
            final @Argument("group") Group group,
            final @Argument("key") String key,
            final @Argument("value") String value
    ) {
        reply(sender, "§aSet option §e" + key + " §ato §f" + value + " §afor §e" + group.name());
    }

    @CommandMethod("info")
    @CommandPermission("pex.group.info")
    public void info(final C sender, final @Argument("group") Group group) {
        reply(sender, "§6Group Info: §e" + group.name());
        reply(sender, "§7  Permissions:");
        for (final var perm : group.permissions()) {
            reply(sender, "§7    - §f" + perm.permission() + " §7(" + perm.value() + ")");
        }
        reply(sender, "§7  Parents:");
        for (final var parent : group.parents()) {
            reply(sender, "§7    - §f" + parent);
        }
    }
}
