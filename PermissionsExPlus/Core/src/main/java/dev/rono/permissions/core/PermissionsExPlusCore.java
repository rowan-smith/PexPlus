package dev.rono.permissions.core;

import dev.rono.permissions.api.PermissionsExPlusApi;
import dev.rono.permissions.api.command.CommandManager;
import dev.rono.permissions.api.event.EventBus;
import dev.rono.permissions.api.group.GroupManager;
import dev.rono.permissions.api.ladder.LadderManager;
import dev.rono.permissions.api.permission.PermissionManager;
import dev.rono.permissions.api.realm.RealmManager;
import dev.rono.permissions.api.user.UserManager;
import dev.rono.permissions.core.cache.GroupCache;
import dev.rono.permissions.core.cache.UserCache;
import dev.rono.permissions.core.command.CloudCommandBootstrap;
import dev.rono.permissions.core.command.CommandRegistry;
import dev.rono.permissions.core.command.exception.CommandExceptionHandler;
import dev.rono.permissions.core.event.EventBusImpl;
import dev.rono.permissions.core.group.GroupManagerImpl;
import dev.rono.permissions.core.ladder.LadderManagerImpl;
import dev.rono.permissions.core.permission.DefaultPermissionResolver;
import dev.rono.permissions.core.permission.PermissionManagerImpl;
import dev.rono.permissions.core.bridge.Platform;
import dev.rono.permissions.core.realm.RealmManagerImpl;
import dev.rono.permissions.core.storage.StorageEngine;
import dev.rono.permissions.core.storage.memory.MemoryStorageEngine;
import dev.rono.permissions.core.user.UserManagerImpl;


public final class PermissionsExPlusCore<C> implements PermissionsExPlusApi {

    private final Platform<C> platform;

    private final UserManager users;
    private final GroupManager groups;
    private final RealmManager realms;
    private final LadderManager ladders;
    private final PermissionManager permissions;
    private final CommandManager commands;
    private final EventBus events;
    private final StorageEngine storage;

    private boolean started = false;

    public PermissionsExPlusCore(Platform<C> platform) {
        this.platform = platform;

        this.platform.logger().info("Creating PermissionsExPlus");

        storage = new MemoryStorageEngine();

        var groups = new GroupManagerImpl(new GroupCache(), storage.groups(), null);
        var resolver = new DefaultPermissionResolver(groups);

        this.commands = new CommandRegistry();
        this.events = new EventBusImpl();
        this.users = new UserManagerImpl(new UserCache(), storage.users(), resolver);
        this.groups = groups;
        this.realms = new RealmManagerImpl(storage.realms());
        this.ladders = new LadderManagerImpl(storage.ladders());
        this.permissions = new PermissionManagerImpl(resolver);
    }

    @Override
    public UserManager users() {
        return users;
    }

    @Override
    public GroupManager groups() {
        return groups;
    }

    @Override
    public LadderManager ladders() {
        return ladders;
    }

    @Override
    public RealmManager realms() {
        return realms;
    }

    @Override
    public PermissionManager permissions() {
        return permissions;
    }

    @Override
    public CommandManager commands() {
        return commands;
    }

    @Override
    public EventBus events() {
        return events;
    }

    public void start() {
        platform.logger().info("Starting PermissionsExPlus");

        if (started) {
            throw new IllegalStateException("Already started");
        }

        storage.open();

        bootstrapCommands();

        started = true;
    }

    public void stop() {
        platform.logger().info("Stopping PermissionsExPlus");

        if (!started) {
            return;
        }

        storage.close();
        started = false;
    }

    private void bootstrapCommands() {
        try {
            var manager = platform.createCommandManager();

            CommandExceptionHandler.register(manager, platform::sendMessage);

            CloudCommandBootstrap.bootstrap(manager, platform.senderType(), this, platform::sendMessage);

            platform.logger().info("Registered Cloud commands");

        } catch (Exception e) {
            throw new RuntimeException("Failed to bootstrap commands", e);
        }
    }
}
