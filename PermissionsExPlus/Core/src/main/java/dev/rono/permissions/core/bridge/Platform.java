package dev.rono.permissions.core.bridge;

import cloud.commandframework.CommandManager;

import java.util.function.BiConsumer;

public interface Platform<C> {
    PlatformLogger logger();

    PlatformScheduler scheduler();

    PlatformConfiguration configuration();

    Class<C> senderType();

    void sendMessage(C sender, String message);

    CommandManager<C> createCommandManager() throws Exception;
}