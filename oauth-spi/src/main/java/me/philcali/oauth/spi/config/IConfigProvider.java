package me.philcali.oauth.spi.config;

import me.philcali.oauth.api.model.IClientConfig;

public interface IConfigProvider {
    IClientConfig getConfig(String apiType);
}
