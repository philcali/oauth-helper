package me.philcali.oauth.spi;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import me.philcali.oauth.api.IAuthManager;
import me.philcali.oauth.api.model.IClientConfig;

public abstract class AbstractOAuthProvider implements OAuthProvider {
    private final Map<String, IAuthManager> managers;

    public AbstractOAuthProvider() {
        this(new ConcurrentHashMap<>());
    }

    public AbstractOAuthProvider(final Map<String, IAuthManager> managers) {
        this.managers = managers;
    }

    @Override
    public IAuthManager createManager(final IClientConfig config, String...params) {
        return managers.computeIfAbsent(config.getClientId(), clientId -> lazilyCreate(config, params));
    }

    @Override
    public IAuthManager getManager(final IClientConfig config) {
        return Optional.ofNullable(managers.get(config.getClientId()))
                .orElseGet(() -> createManager(config));
    }

    protected abstract IAuthManager lazilyCreate(final IClientConfig config, String...params);
}
