package me.philcali.oauth.api.model;

import java.util.Map;

import me.philcali.zero.lombok.annotation.Data;

@Data
public interface IToken {
    String getAccessToken();
    String getApi();
    String getTokenType();
    Map<String, String> getParams();
}
