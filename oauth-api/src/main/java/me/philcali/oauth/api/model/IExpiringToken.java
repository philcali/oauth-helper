package me.philcali.oauth.api.model;

import me.philcali.zero.lombok.annotation.Data;

@Data
public interface IExpiringToken extends IToken {
    String getClientId();
    long getExpiresIn();
    String getRefreshToken();
}
