package dev.rono.permissions.core.command;

import dev.rono.permissions.api.command.CommandManager;
import dev.rono.permissions.api.command.PermissionCommand;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class CommandRegistry implements CommandManager {
    private final Map<String, PermissionCommand> commands = new HashMap<>();

    @Override
    public void register(final PermissionCommand command) {
        commands.put(command.name().toLowerCase(), command);
    }

    @Override
    public Optional<PermissionCommand> find(final String name) {
        return Optional.ofNullable(commands.get(name.toLowerCase()));
    }

    @Override
    public Collection<PermissionCommand> commands() {
        return Collections.unmodifiableCollection(commands.values());
    }
}
