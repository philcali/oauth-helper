package me.philcali.oauth.api.model;

public interface IToken {
    String getAccessToken();
    String getApi();
    String getTokenType();
}
