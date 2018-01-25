package me.philcali.oauth.spi.config;

import java.util.Objects;
import java.util.Optional;

import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.api.transfer.ClientConfig;

public class SystemEnvConfigProvider implements IConfigProvider {

    private String checkOrThrow(final String key) {
        return Objects.requireNonNull(System.getenv(key), "Missing " + key);
    }

    @Override
    public IClientConfig getConfig(final String apiType) {
        final String systemFormat = new StringBuilder(apiType.toUpperCase())
                .append("_%s")
                .toString();
        return ClientConfig.builder()
                .withApi(apiType)
                .withClientId(checkOrThrow(String.format(systemFormat, "CLIENT_ID")))
                .withClientSecret(checkOrThrow(String.format(systemFormat, "CLIENT_SECRET")))
                .withRedirectUrl(System.getenv(String.format(systemFormat, "REDIRECT_URL")))
                .withScopes(getScopes(String.format(systemFormat, "SCOPES")))
                .build();
    }

    private String[] getScopes(final String key) {
        return Optional.ofNullable(System.getenv(key)).orElse("").split("\\s*,\\s*");
    }
}
