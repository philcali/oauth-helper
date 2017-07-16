package me.philcali.oauth.api;

public interface IExpiringToken extends IToken {
    long getExpiresIn();
    String getRefreshToken();
}
