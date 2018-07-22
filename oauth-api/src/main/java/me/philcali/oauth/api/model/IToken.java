package me.philcali.oauth.api.model;

import java.util.Map;

public interface IToken {
    String getAccessToken();
    String getApi();
    String getTokenType();
    Map<String, String> getParams();
}
