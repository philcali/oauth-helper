package me.philcali.oauth.api;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.api.model.IExpiringToken;
import me.philcali.oauth.api.model.IToken;

public interface ITokenRepository {
    IExpiringToken generate(IClientConfig config, Map<String, String> params);

    Optional<IExpiringToken> get(String api, String tokenId);

    void put(IToken token);

    default IExpiringToken generate(IClientConfig config) {
        return generate(config, Collections.emptyMap());
    }
}
