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

    private static List<OAuthProvider> loadProviders(final ClassLoader loader) {
        final ServiceLoader<OAuthProvider> providers = ServiceLoader.load(
                OAuthProvider.class,
                Optional.ofNullable(loader).orElseGet(ClassLoader::getSystemClassLoader));
        return StreamSupport.stream(providers.spliterator(), false).collect(Collectors.toList());
    }

    public static IAuthManager newAuthManager(final IClientConfig config, final ClassLoader loader,
            final String...params) {
        final Function<OAuthProvider, IAuthManager> thunk = provider -> provider.createManager(config, params);
        final Optional<IAuthManager> optionalManager = findAuthProvider(config).map(thunk);
        return optionalManager
                .orElseGet(() -> Optional.ofNullable(loader)
                        .map(OAuthProviders::loadProviders)
                        .flatMap(providers -> findAuthProvider(config, providers).map(thunk))
                        .orElseThrow(() -> new OAuthProviderNotFoundException("Provider of " + config.getApi()
                                + " not found.")));
    }

    public static IAuthManager newAuthManager(final IClientConfig config, final String...params) {
        return newAuthManager(config, null, params);
    }

    private OAuthProviders() {
    }
}
