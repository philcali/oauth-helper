package me.philcali.oauth.api;

import java.util.ArrayList;
import java.util.List;

public class ClientConfig {
    private static final int DEFAULT_CONNECT = 5;
    private static final int DEFAULT_READ = 30;

    private String api;
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
    private List<String> scopes;
    private int connectTimeout = DEFAULT_CONNECT;
    private int readTimeout = DEFAULT_READ;

    public ClientConfig addScope(String scope) {
        getScopes().add(scope);
        return this;
    }

    public String getApi() {
        return api;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public List<String> getScopes() {
        if (scopes == null) {
            scopes = new ArrayList<>();
        }
        return scopes;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public ClientConfig withApi(String api) {
        setApi(api);
        return this;
    }

    public ClientConfig withClientId(String clientId) {
        setClientId(clientId);
        return this;
    }

    public ClientConfig withClientSecret(String clientSecret) {
        setClientSecret(clientSecret);
        return this;
    }

    public ClientConfig withConnectTimeout(int connectTimeout) {
        setConnectTimeout(connectTimeout);
        return this;
    }

    public ClientConfig withReadTimeout(int readTimeout) {
        setReadTimeout(readTimeout);
        return this;
    }

    public ClientConfig withRedirectUrl(String redirectUrl) {
        setRedirectUrl(redirectUrl);
        return this;
    }

    public ClientConfig withScopes(List<String> scopes) {
        setScopes(scopes);
        return this;
    }
}
