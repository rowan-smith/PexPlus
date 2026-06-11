package dev.rono.permissions.api.service;

import dev.rono.permissions.api.PermissionsExException;
import dev.rono.permissions.api.backend.BackendInfo;
import dev.rono.permissions.api.subject.Group;
import dev.rono.permissions.api.subject.User;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Modern PermissionsEx integration API ({@code dev.rono.permissions.api}).
 *
 * <p>Registered on Spigot/Paper {@code ServicesManager} under this type. Implemented by the runtime
 * manager alongside legacy {@code ru.tehkode.permissions.PermissionManager}.</p>
 */
public interface PermissionService {

    // --- Introspection (legacy aliases retained) ---

    default int registeredUserNameCount() {
        return userCount();
    }

    default int registeredGroupCount() {
        return groupCount();
    }

    default String activeBackendSimpleName() {
        return backend().simpleName();
    }

    BackendInfo backend();

    int userCount();

    int groupCount();

    Collection<String> worlds();

    boolean isDebug();

    // --- Permission checks ---

    boolean has(UUID playerId, String permission);

    boolean has(UUID playerId, String permission, String world);

    boolean has(String playerName, String permission, String world);

    // --- Users ---

    Optional<User> findUser(String identifier);

    Optional<User> findUser(UUID uuid);

    /** Resolves or materializes a user (classic {@code getUser} semantics). */
    User user(String identifier);

    User user(UUID uuid);

    Set<String> userIdentifiers();

    void deleteUser(String identifier);

    // --- Groups ---

    Optional<Group> findGroup(String name);

    /** Resolves a persisted group; throws if the group does not exist in the backend. */
    Group group(String name);

    Set<String> groupNames();

    void deleteGroup(String name);

    // --- Maintenance ---

    void reload() throws PermissionsExException;
}
