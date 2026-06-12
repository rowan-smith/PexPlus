package dev.rono.permissions.api.bus;

import java.util.UUID;

public record PexSystemDispatch(UUID sourceId, PexSystemMutation mutation) implements PexPermissionDispatch {}
