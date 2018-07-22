package me.philcali.oauth.google;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import me.philcali.oauth.api.model.IExpiringToken;

public class AuthToken implements IExpiringToken {
    private static final String APPLICATION = "google";
    private final Map<String, Object> data;
    private final String refreshToken;
    private final String clientId;

    public AuthToken(Map<String, Object> data) {
        this(null, null, data);
    }

    public AuthToken(String refreshToken, String clientId, Map<String, Object> data) {
        this.refreshToken = refreshToken;
        this.clientId = clientId;
        this.data = data;
    }

    @Override
    public String getAccessToken() {
        return data.get("access_token").toString();
    }

    @Override
    public String getApi() {
        return APPLICATION;
    }

    @Override
    public String getClientId() {
        return data.getOrDefault("client_id", clientId).toString();
    }

    @Override
    public long getExpiresIn() {
        return (System.currentTimeMillis() / 1000) + ((Integer) data.get("expires_in")).longValue();
    }

    @Override
    public String getRefreshToken() {
        return Optional.ofNullable(data.get("refresh_token"))
                .map(token -> (String) token)
                .orElse(refreshToken);
    }

    @Override
    public String getTokenType() {
        return data.get("token_type").toString();
    }

    @Override
    public Map<String, String> getParams() {
        return Collections.emptyMap();
    }

}
