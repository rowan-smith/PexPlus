package dev.rono.permissions.core.api.pex;

import dev.rono.permissions.api.permission.PermissionAddRequest;
import dev.rono.permissions.api.permission.PermissionHolder;
import dev.rono.permissions.api.permission.PermissionNode;
import dev.rono.permissions.api.permission.PermissionService;
import dev.rono.permissions.api.permission.PermissionSource;
import dev.rono.permissions.core.DefaultPermissionManager;
import ru.tehkode.permissions.PermissionEntity;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class HolderPermissionService implements PermissionService {

    private final DefaultPermissionManager manager;
    private final HolderEntityResolver resolver;

    HolderPermissionService(DefaultPermissionManager manager) {
        this.manager = manager;
        this.resolver = new HolderEntityResolver(manager);
    }

    @Override
    public PermissionNode addPermission(PermissionHolder holder, String permission) {
        return addPermission(holder, permission, null);
    }

    @Override
    public PermissionNode addPermission(PermissionHolder holder, String permission, Duration duration) {
        PermissionEntity entity = resolver.resolve(holder);
        String world = null;
        if (duration == null) {
            entity.addPermission(permission, world);
            return new PermissionNodeImpl(holder, permission, null, Map.of(), PermissionSource.SYSTEM);
        }
        int seconds = (int) Math.min(Integer.MAX_VALUE, duration.getSeconds());
        entity.addTimedPermission(permission, world, seconds);
        Instant expiresAt = Instant.now().plus(duration);
        return new PermissionNodeImpl(holder, permission, expiresAt, Map.of(), PermissionSource.SYSTEM);
    }

    @Override
    public PermissionNode addPermission(PermissionAddRequest request) {
        PermissionEntity entity = resolver.resolve(request.holder());
        String world = resolver.worldContext(request.context());
        Instant expiresAt = request.expiresAt();
        if (expiresAt != null) {
            int seconds = (int) Math.max(1, expiresAt.getEpochSecond() - Instant.now().getEpochSecond());
            entity.addTimedPermission(request.permission(), world, seconds);
        } else {
            entity.addPermission(request.permission(), world);
        }
        return new PermissionNodeImpl(
                request.holder(),
                request.permission(),
                expiresAt,
                request.context(),
                request.source());
    }

    @Override
    public void removePermission(PermissionHolder holder, String permission) {
        PermissionEntity entity = resolver.resolve(holder);
        entity.removePermission(permission, null);
    }

    @Override
    public boolean hasPermission(PermissionHolder holder, String permission) {
        PermissionEntity entity = resolver.resolve(holder);
        return entity.has(permission, null);
    }

    @Override
    public List<PermissionNode> getPermissions(PermissionHolder holder) {
        PermissionEntity entity = resolver.resolve(holder);
        List<PermissionNode> nodes = new ArrayList<>();
        for (String perm : entity.getOwnPermissions(null)) {
            nodes.add(new PermissionNodeImpl(holder, perm, null, Map.of(), PermissionSource.SYSTEM));
        }
        return List.copyOf(nodes);
    }
}
