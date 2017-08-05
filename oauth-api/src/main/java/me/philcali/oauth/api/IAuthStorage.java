package me.philcali.oauth.api;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import me.philcali.oauth.api.model.IAuthNonce;


public interface IAuthStorage {
    default IAuthNonce generateAuthNonce(String api) {
        return generateAuthNonce(api, Collections.emptyMap());
    }

    IAuthNonce generateAuthNonce(String api, Map<String, String> params);

    IExpiringToken generateSessionToken(ClientConfig config);

    Optional<ClientConfig> getConfig(String api, String clientId);

    Stream<ClientConfig> getConfigsForOwner(String email);

    Optional<IExpiringToken> getSession(String api, String tokenId);

    Optional<ClientConfig> save(String email, ClientConfig config);

    default Optional<IAuthNonce> verifyNonce(String id, String api) {
        return verifyNonce(id, api, System.currentTimeMillis());
    }

    Optional<IAuthNonce> verifyNonce(String id, String api, long timestamp);
}
