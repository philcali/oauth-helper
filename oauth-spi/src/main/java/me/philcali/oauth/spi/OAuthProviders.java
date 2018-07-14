package me.philcali.oauth.spi;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import me.philcali.oauth.api.IAuthManager;
import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.spi.config.DefaultConfigProviderChain;
import me.philcali.oauth.spi.config.IConfigProvider;

public final class OAuthProviders {
    private static final List<OAuthProvider> DEFAULT_PROVIDERS;
    static {
        DEFAULT_PROVIDERS = Collections.unmodifiableList(loadProviders(null));
    }

    private static Optional<OAuthProvider> findAuthProvider(final IClientConfig config) {
        return findAuthProvider(config, DEFAULT_PROVIDERS);
    }

    private static Optional<OAuthProvider> findAuthProvider(final IClientConfig config,
            final List<OAuthProvider> providers) {
        return providers.stream()
                .filter(provider -> provider.getApiType().equalsIgnoreCase(config.getApi()))
                .findFirst();
    }

    public static IAuthManager getAuthManager(final IClientConfig config) {
        return findAuthProvider(config)
                .map(provider -> provider.getManager(config))
                .orElseThrow(() -> new OAuthProviderNotFoundException("Provider of " + config.getApi()
                        + " not found."));
    }

    public static <T extends IAuthManager> T getAuthManager(final String api, final Class<T> authClass) {
        return getAuthManager(api, new DefaultConfigProviderChain(), authClass);
    }

    public static <T extends IAuthManager> T getAuthManager(final String api, final IConfigProvider provider,
            final Class<T> authClass) {
        return authClass.cast(getAuthManager(provider.getConfig(api)));
    }


    private static List<OAuthProvider> loadProviders(final ClassLoader loader) {
        final ServiceLoader<OAuthProvider> providers = ServiceLoader.load(
                OAuthProvider.class,
                Optional.ofNullable(loader).orElseGet(ClassLoader::getSystemClassLoader));
        return StreamSupport.stream(providers.spliterator(), false).collect(Collectors.toList());
    }

    public static <T extends IAuthManager> T newAuthManager(final IClientConfig config, final ClassLoader loader,
            final Class<T> authClass, final String...params) {
        final Function<OAuthProvider, IAuthManager> thunk = provider -> provider.createManager(config, params);
        final Optional<IAuthManager> optionalManager = findAuthProvider(config, loadProviders(loader)).map(thunk);
        return optionalManager.map(authClass::cast)
                        .orElseThrow(() -> new OAuthProviderNotFoundException("Provider of " + config.getApi()
                                + " not found."));
    }

    public static <T extends IAuthManager> T newAuthManager(final IClientConfig config, final Class<T> authClass,
            final String...params) {
        return newAuthManager(config, null, authClass, params);
    }

    private OAuthProviders() {
    }
}
