package me.philcali.oauth.api.model;

public interface IExpiringToken extends IToken {
    long getExpiresIn();
    String getRefreshToken();
}
