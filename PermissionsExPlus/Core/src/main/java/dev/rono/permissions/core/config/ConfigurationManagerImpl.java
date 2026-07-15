package dev.rono.permissions.core.config;

import dev.rono.permissions.api.config.ConfigurationManager;
import dev.rono.permissions.core.platform.PlatformConfiguration;

public record ConfigurationManagerImpl(GeneralConfiguration general, BackendConfiguration backend, AdvancedConfiguration advanced) implements ConfigurationManager {

    public static ConfigurationManagerImpl load(PlatformConfiguration platform) {
        var general = GeneralConfiguration.load(platform);
        var advanced = AdvancedConfiguration.load(platform);
        var backend = BackendConfiguration.load(platform);

        return new ConfigurationManagerImpl(general, backend, advanced);
    }

    public boolean preloadOnJoin() {
        return advanced.preloadOnJoin();
    }

    public boolean vaultEnabled() {
        return general.vaultEnabled();
    }

    public boolean placeholderApiEnabled() {
        return general.placeholderApiEnabled();
    }
}
