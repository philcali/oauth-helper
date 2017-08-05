package me.philcali.oauth.dynamo.model;

import com.amazonaws.services.dynamodbv2.document.Item;

import me.philcali.oauth.api.ClientConfig;

public class ClientConfigDynamo extends ClientConfig {
    private String email;

    public ClientConfigDynamo(Item item) {
        email = item.getString("email");
        setApi(item.getString("api"));
        setClientId(item.getString("clientId"));
        setClientSecret(item.getString("clientSecret"));
        setRedirectUrl(item.getString("redirectUrl"));
        setScopes(item.getList("scopes"));
        setConnectTimeout(item.getInt("connectTimeout"));
        setReadTimeout(item.getInt("readTimeout"));
    }

    public String getEmail() {
        return email;
    }
}
