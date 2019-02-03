package me.philcali.oauth.api.model;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.philcali.zero.lombok.annotation.Builder;
import me.philcali.zero.lombok.annotation.Data;

@Data @Builder
public interface IClientConfig {
    @Builder.Default int DEFAULT_CONNECT_TIMEOUT = (int) TimeUnit.SECONDS.toMillis(5);
    @Builder.Default int DEFAULT_READ_TIMEOUT = (int) TimeUnit.SECONDS.toMillis(30);

    String getApi();
    String getClientId();
    String getClientSecret();
    int getConnectTimeout();
    int getReadTimeout();
    String getRedirectUrl();
    List<String> getScopes();
}
