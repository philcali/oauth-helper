package me.philcali.oauth.api.model;

public interface IExpiringToken extends IToken {
    String getClientId();
    long getExpiresIn();
    String getRefreshToken();
}
