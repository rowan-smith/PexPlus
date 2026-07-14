package dev.rono.permissions.api.command;

import java.util.Collection;
import java.util.Optional;

public interface CommandManager {
    void register(PermissionCommand command);

    Optional<PermissionCommand> find(String name);

    Collection<PermissionCommand> commands();
}
