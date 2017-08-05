package me.philcali.oauth.api.model;

import java.util.Map;

public interface IAuthNonce {
    String getApi();

    long getExpiresIn();

    String getId();

    Map<String, String> getParams();
}
