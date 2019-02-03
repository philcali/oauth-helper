package me.philcali.oauth.api.model;

import me.philcali.zero.lombok.annotation.Builder;
import me.philcali.zero.lombok.annotation.Data;

@Data @Builder
public interface IUserClientConfig extends IClientConfig {
    String getUserId();
}
