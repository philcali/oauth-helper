package me.philcali.oauth.api;

public interface IToken {
    String getAccessToken();
    String getApi();
    String getTokenType();
}
