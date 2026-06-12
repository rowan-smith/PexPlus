package dev.rono.permissions.bukkit;

import dev.rono.permissions.api.service.PexPermissionService;
import org.bukkit.Bukkit;
import ru.tehkode.permissions.PermissionManager;

/**
 * Internal helper that resolves {@link PexPermissionService} from Bukkit {@code ServicesManager}.
 */
final class PexServices {

    private PexServices() {}

    /**
     * @return registered {@link PexPermissionService} provider
     * @throws IllegalStateException if PermissionsEx is not loaded or the service is not registered
     */
    static PexPermissionService require() {
        var managerReg = Bukkit.getServer().getServicesManager().getRegistration(PermissionManager.class);
        if (managerReg != null && managerReg.getProvider() instanceof PexPermissionService service) {
            return service;
        }
        var reg = Bukkit.getServer().getServicesManager().getRegistration(PexPermissionService.class);
        if (reg == null) {
            throw new IllegalStateException("PermissionsEx is not registered — is PermissionsEx loaded?");
        }
        return reg.getProvider();
    }
}
