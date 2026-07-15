package dev.rono.permissions.core.command;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import dev.rono.permissions.core.PexApiImpl;

import java.util.function.BiConsumer;

@CommandMethod("pex ladders")
public final class LaddersCommands<C> extends CommandSupport<C> {
    LaddersCommands(PexApiImpl<C> core, BiConsumer<C, String> messages) {
        super(core, messages);
    }

    @CommandMethod("")
    @CommandPermission("pex.ladders.list")
    public void list(C sender) {
        section(sender, "Ladders", await(core.ladders().storage().identifiers()));
    }

    @CommandMethod("create <ladder>")
    @CommandPermission("pex.ladders.create")
    public void create(C sender, @Argument("ladder") String name) {
        await(core.ladders().create(name));

        audit(sender, "CREATED ladder '" + name + "'");

        reply(sender, "§aCreated ladder §e" + name);
    }

    @CommandMethod("delete <ladder>")
    @CommandPermission("pex.ladders.delete")
    public void delete(C sender, @Argument(value = "ladder", suggestions = "ladders") String name) {
        await(core.ladders().delete(name));

        reply(sender, "§aDeleted ladder §e" + name);
    }
}
