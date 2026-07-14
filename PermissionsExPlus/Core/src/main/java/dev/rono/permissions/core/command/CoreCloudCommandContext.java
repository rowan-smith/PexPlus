package dev.rono.permissions.core.command;

import cloud.commandframework.CommandManager;
import dev.rono.permissions.api.PermissionsExPlusApi;

import java.util.function.BiConsumer;

public final class CoreCloudCommandContext<C> {

    private final CommandManager<C> manager;
    private final PermissionsExPlusApi api;
    private final BiConsumer<C, String> messageSender;

    public CoreCloudCommandContext(final CommandManager<C> manager, final PermissionsExPlusApi api, final BiConsumer<C, String> messageSender) {
        this.manager = manager;
        this.api = api;
        this.messageSender = messageSender;
    }

    public CommandManager<C> manager() {
        return manager;
    }

    public PermissionsExPlusApi api() {
        return api;
    }

    public void send(final C sender, final String message) {
        messageSender.accept(sender, message);
    }
}
