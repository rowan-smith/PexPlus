package dev.rono.permissions.api.bus;

import java.util.UUID;

/**
 * @param sourceId        logical server / world container id
 * @param entityIdentifier stable user or group key
 * @param entityType      e.g. USER or GROUP
 */
public record PexEntityDispatch(UUID sourceId, String entityIdentifier, String entityType, PexEntityMutation mutation)
        implements PexPermissionDispatch {}
