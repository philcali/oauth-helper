package me.philcali.oauth.google;

import me.philcali.oauth.api.IAuthManager;
import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.spi.AbstractOAuthProvider;

public class OAuthProviderGoogle extends AbstractOAuthProvider {
    private static final String TYPE = "google";

    @Override
    public String getApiType() {
        return TYPE;
    }

    @Override
    protected IAuthManager lazilyCreate(final IClientConfig config, String...params) {
        return new AuthManagerGoogle(config);
    }
}
