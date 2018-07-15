package me.philcali.oauth.api.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.philcali.oauth.api.model.IClientConfig;

public class ClientConfig implements IClientConfig {
    public static class Builder {
        private String api;
        private String clientId;
        private String clientSecret;
        private String redirectUrl;
        private List<String> scopes = new ArrayList<>();
        private int connectTimeout = DEFAULT_CONNECT;
        private int readTimeout = DEFAULT_READ;

        public ClientConfig build() {
            return new ClientConfig(this);
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
            return scopes;
        }

        public Builder withApi(String api) {
            this.api = api;
            return this;
        }

        public Builder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder withClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder withConnectTimeout(final int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder withReadTimeout(final int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        public Builder withRedirectUrl(String redirectUrl) {
            this.redirectUrl = redirectUrl;
            return this;
        }

        public Builder withScopes(final List<String> scopes) {
            this.scopes = scopes;
            return this;
        }

        public Builder withScopes(final String...scopes) {
            Arrays.stream(scopes).forEach(this.scopes::add);
            return this;
        }

    }

    private static final int DEFAULT_CONNECT = (int) TimeUnit.SECONDS.toMillis(5);
    private static final int DEFAULT_READ = (int) TimeUnit.SECONDS.toMillis(30);

    public static Builder builder() {
        return new Builder();
    }

    private String api;
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
    private List<String> scopes;
    private int connectTimeout;
    private int readTimeout;

    public ClientConfig() {
    }

    public ClientConfig(final Builder builder) {
        this.api = builder.getApi();
        this.readTimeout = builder.getReadTimeout();
        this.connectTimeout = builder.getConnectTimeout();
        this.clientId = builder.getClientId();
        this.clientSecret = builder.getClientSecret();
        this.scopes = builder.getScopes();
        this.redirectUrl = builder.getRedirectUrl();
    }

    @Override
    public String getApi() {
        return api;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public int getConnectTimeout() {
        return connectTimeout;
    }

    @Override
    public int getReadTimeout() {
        return readTimeout;
    }

    @Override
    public String getRedirectUrl() {
        return redirectUrl;
    }

    @Override
    public List<String> getScopes() {
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
}
