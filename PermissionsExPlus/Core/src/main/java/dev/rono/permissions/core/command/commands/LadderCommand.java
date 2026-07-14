package dev.rono.permissions.core.command.commands;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import dev.rono.permissions.api.group.Group;
import dev.rono.permissions.api.ladder.Ladder;
import dev.rono.permissions.api.realm.BuiltinRealm;
import dev.rono.permissions.api.user.User;
import dev.rono.permissions.core.command.AbstractCloudCommand;
import dev.rono.permissions.core.command.CoreCloudCommandContext;

@CommandMethod("pex")
public final class LadderCommand<C> extends AbstractCloudCommand<C> {

    public LadderCommand(final CoreCloudCommandContext<C> ctx) {
        super(ctx);
    }

    @CommandMethod("ladders")
    @CommandPermission("pex.ladders")
    public void ladders(final C sender) {
        reply(sender, "§6Ladders:");
        for (final var ladder : ctx.api().ladders().all()) {
            reply(sender, "§7  - §f" + ladder.name());
        }
    }

    @CommandMethod("ladder <ladder> info")
    @CommandPermission("pex.ladder.info")
    public void info(final C sender, final @Argument(value = "ladder", suggestions = "pex-ladder") Ladder ladder) {
        reply(sender, "§6Ladder Info: §e" + ladder.name());
        reply(sender, "§7  Groups:");

        final var groups = ladder.groups();
        for (int i = 0; i < groups.size(); i++) {
            reply(sender, "§7    " + (i + 1) + ". §f" + groups.get(i).name());
        }
    }

    @CommandMethod("ladder <ladder> promote <user>")
    @CommandPermission("pex.ladder.promote")
    public void promote(
            final C sender,
            final @Argument(value = "ladder", suggestions = "pex-ladder") Ladder ladder,
            final @Argument(value = "user", suggestions = "pex-user") User user
    ) {
        final var result = ctx.api().ladders().promote(user, ladder, BuiltinRealm.GLOBAL);

        final var msg = switch (result) {
            case PROMOTED -> "\u2714 Promoted §e" + user.name();
            case ALREADY_TOP -> "§e" + user.name() + " §cis already at the top of §e" + ladder.name();
            case NOT_IN_LADDER -> "§e" + user.name() + " §cis not on ladder §e" + ladder.name();
            default -> "§cCould not promote §e" + user.name();
        };

        reply(sender, msg);
    }

    @CommandMethod("ladder <ladder> demote <user>")
    @CommandPermission("pex.ladder.demote")
    public void demote(
            final C sender,
            final @Argument(value = "ladder", suggestions = "pex-ladder") Ladder ladder,
            final @Argument(value = "user", suggestions = "pex-user") User user
    ) {
        final var result = ctx.api().ladders().demote(user, ladder, BuiltinRealm.GLOBAL);
        final var msg = switch (result) {
            case DEMOTED -> "\u2714 Demoted §e" + user.name();
            case ALREADY_BOTTOM -> "§e" + user.name() + " §cis already at the bottom of §e" + ladder.name();
            case NOT_IN_LADDER -> "§e" + user.name() + " §cis not on ladder §e" + ladder.name();
            default -> "§cCould not demote §e" + user.name();
        };

        reply(sender, msg);
    }

    @CommandMethod("ladder <ladder> groups list")
    @CommandPermission("pex.ladder.groups.list")
    public void groupsList(final C sender, final @Argument(value = "ladder", suggestions = "pex-ladder") Ladder ladder) {
        reply(sender, "§6Groups in §e" + ladder.name() + "§6:");

        final var groups = ladder.groups();
        for (int i = 0; i < groups.size(); i++) {
            reply(sender, "§7    " + (i + 1) + ". §f" + groups.get(i).name());
        }
    }

    @CommandMethod("ladder <ladder> groups add <group>")
    @CommandPermission("pex.ladder.groups.add")
    public void groupsAdd(
            final C sender,
            final @Argument(value = "ladder", suggestions = "pex-ladder") Ladder ladder,
            final @Argument(value = "group", suggestions = "pex-group") Group group
    ) {
        ctx.api().ladders().addGroup(ladder, group);
        reply(sender, "§aAdded group §f" + group.name() + " §ato ladder §e" + ladder.name());
    }

    @CommandMethod("ladder <ladder> groups remove <group>")
    @CommandPermission("pex.ladder.groups.remove")
    public void groupsRemove(
            final C sender,
            final @Argument(value = "ladder", suggestions = "pex-ladder") Ladder ladder,
            final @Argument(value = "group", suggestions = "pex-group") Group group
    ) {
        ctx.api().ladders().removeGroup(ladder, group);
        reply(sender, "§aRemoved group §f" + group.name() + " §afrom ladder §e" + ladder.name());
    }

    @CommandMethod("ladder <ladder> groups move <group> <position>")
    @CommandPermission("pex.ladder.groups.move")
    public void groupsMove(
            final C sender,
            final @Argument(value = "ladder", suggestions = "pex-ladder") Ladder ladder,
            final @Argument(value = "group", suggestions = "pex-group") Group group,
            final @Argument("position") int position
    ) {
        reply(sender, "§eGroup " + group.name() + " moved on ladder " + ladder.name() + " (stub)");
    }
}
