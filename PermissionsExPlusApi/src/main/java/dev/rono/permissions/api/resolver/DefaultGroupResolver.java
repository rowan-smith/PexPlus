package dev.rono.permissions.api.resolver;

import dev.rono.permissions.api.group.Group;

import java.util.Optional;

/**
 * Resolves the configured implicit fallback group. The fallback is calculated
 * in memory and is never persisted as a user membership.
 */
public interface DefaultGroupResolver {

    Optional<Group> resolve();
}
