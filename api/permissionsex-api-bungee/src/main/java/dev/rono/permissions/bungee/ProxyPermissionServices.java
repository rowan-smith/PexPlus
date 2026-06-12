package dev.rono.permissions.bungee;

import dev.rono.permissions.api.service.PexPermissionService;
import ru.tehkode.permissions.PermissionManager;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service registry for Bungee/Waterfall (no Bukkit {@code ServicesManager}).
 *
 * <p>Hook plugins should use {@link PermissionsEx#getApi()}.</p>
 */
public final class ProxyPermissionServices {
    private static final AtomicReference<PermissionManager> PERMISSION_MANAGER = new AtomicReference<>();
    private static final AtomicReference<PexPermissionService> PERMISSION_SERVICE = new AtomicReference<>();

    private ProxyPermissionServices() {}

    public static void register(PermissionManager manager, PexPermissionService service) {
        PERMISSION_MANAGER.set(Objects.requireNonNull(manager, "manager"));
        PERMISSION_SERVICE.set(Objects.requireNonNull(service, "service"));
    }

    public static void unregister() {
        PERMISSION_MANAGER.set(null);
        PERMISSION_SERVICE.set(null);
    }

    public static boolean isRegistered() {
        return PERMISSION_MANAGER.get() != null;
    }

    public static PermissionManager permissionManager() {
        PermissionManager manager = PERMISSION_MANAGER.get();
        if (manager == null) {
            throw new IllegalStateException("PermissionManager is not registered on this proxy");
        }
        return manager;
    }

    public static PexPermissionService permissionService() {
        PexPermissionService service = PERMISSION_SERVICE.get();
        if (service == null) {
            throw new IllegalStateException("PexPermissionService is not registered on this proxy");
        }
        return service;
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> type) {
        Objects.requireNonNull(type, "type");
        if (PermissionManager.class.equals(type)) {
            return (T) permissionManager();
        }
        if (PexPermissionService.class.equals(type)) {
            return (T) permissionService();
        }
        throw new IllegalArgumentException("Unsupported service type on proxy: " + type.getName());
    }
}
