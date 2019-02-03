package me.philcali.oauth.api.model;

import java.util.Map;

import me.philcali.zero.lombok.annotation.Data;

@Data
public interface IAuthNonce {
    String getApi();
    long getExpiresIn();
    String getId();
    Map<String, String> getParams();
}
