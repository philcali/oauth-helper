package me.philcali.oauth.spi;

import me.philcali.oauth.api.IAuthManager;
import me.philcali.oauth.api.model.IClientConfig;

public interface OAuthProvider {

    IAuthManager createManager(IClientConfig config, String...params);

    String getApiType();

    IAuthManager getManager(IClientConfig config);
}
