package me.philcali.oauth.spi.config;

import java.util.Arrays;
import java.util.List;

import me.philcali.oauth.api.model.IClientConfig;

public class DefaultConfigProviderChain implements IConfigProvider {
    private final List<IConfigProvider> providers;

    public DefaultConfigProviderChain() {
        this(Arrays.asList(
                new SystemEnvConfigProvider(),
                new SystemPropertyConfigProvider()));
    }

    public DefaultConfigProviderChain(final List<IConfigProvider> providers) {
        this.providers = providers;
    }

    @Override
    public IClientConfig getConfig(final String apiType) {
        for (IConfigProvider provider : providers) {
            try {
                return provider.getConfig(apiType);
            } catch (NullPointerException npe) {
            }
        }
        throw new IllegalStateException("Could not load a default config!");
    }

}
