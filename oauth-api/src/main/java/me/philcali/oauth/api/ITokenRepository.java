package me.philcali.oauth.api;

import java.util.Optional;

import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.api.model.IExpiringToken;

public interface ITokenRepository {
    IExpiringToken generate(IClientConfig config);

    Optional<IExpiringToken> get(String api, String tokenId);
}
