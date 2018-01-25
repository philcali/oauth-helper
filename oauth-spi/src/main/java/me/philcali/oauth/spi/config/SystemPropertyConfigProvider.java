package me.philcali.oauth.spi.config;

import java.util.Objects;

import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.api.transfer.ClientConfig;

public class SystemPropertyConfigProvider implements IConfigProvider {

    private String checkOrThrow(final String key) {
        return Objects.requireNonNull(System.getProperty(key), "Missing " + key);
    }

    @Override
    public IClientConfig getConfig(final String apiType) {
        final String systemFormat = new StringBuilder(apiType.toLowerCase())
                .append(".%s")
                .toString();
        return ClientConfig.builder()
                .withApi(apiType)
                .withClientId(checkOrThrow(String.format(systemFormat, "clientId")))
                .withClientSecret(checkOrThrow(String.format(systemFormat, "clientSecret")))
                .withRedirectUrl(System.getProperty(String.format(systemFormat, "redirectUrl")))
                .withScopes(getScopes(String.format(systemFormat, "scopes")))
                .build();
    }

    private String[] getScopes(final String key) {
        return System.getProperty(key, "").split("\\s*,\\s*");
    }
}
