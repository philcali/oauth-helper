package me.philcali.oauth.api.model;

import java.util.List;

public interface IClientConfig {
    String getApi();
    String getClientId();
    String getClientSecret();
    int getConnectTimeout();
    int getReadTimeout();
    String getRedirectUrl();
    List<String> getScopes();
}
