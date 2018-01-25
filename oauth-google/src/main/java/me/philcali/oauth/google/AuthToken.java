package me.philcali.oauth.google;

import java.util.Map;

import me.philcali.oauth.api.model.IExpiringToken;

public class AuthToken implements IExpiringToken {
    private static final String APPLICATION = "GOOGLE";
    private final Map<String, String> data;
    private final String refreshToken;
    private final String clientId;

    public AuthToken(Map<String, String> data) {
        this(null, null, data);
    }

    public AuthToken(String refreshToken, String clientId, Map<String, String> data) {
        this.refreshToken = refreshToken;
        this.clientId = clientId;
        this.data = data;
    }

    @Override
    public String getAccessToken() {
        return data.get("access_token");
    }

    @Override
    public String getApi() {
        return APPLICATION;
    }

    @Override
    public String getClientId() {
        return data.getOrDefault("client_id", clientId);
    }

    @Override
    public long getExpiresIn() {
        return Long.parseLong(data.get("expires_in"));
    }

    @Override
    public String getRefreshToken() {
        return data.getOrDefault("refresh_token", refreshToken);
    }

    @Override
    public String getTokenType() {
        return data.get("token_type");
    }

}
