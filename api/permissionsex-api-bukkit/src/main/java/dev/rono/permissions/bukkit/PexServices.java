package dev.rono.permissions.bukkit;

import dev.rono.permissions.api.PermissionsExApi;
import dev.rono.permissions.api.service.PexPermissionService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

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
        RegisteredServiceProvider<PermissionsExApi> apiReg =
                Bukkit.getServer().getServicesManager().getRegistration(PermissionsExApi.class);
        if (apiReg != null && apiReg.getProvider() != null) {
            return (PexPermissionService) apiReg.getProvider().getLegacyPermissionManager();
        }
        RegisteredServiceProvider<PexPermissionService> reg =
                Bukkit.getServer().getServicesManager().getRegistration(PexPermissionService.class);
        if (reg == null) {
            throw new IllegalStateException("PermissionsEx is not registered — is PermissionsEx loaded?");
        }
        return reg.getProvider();
    }
}
