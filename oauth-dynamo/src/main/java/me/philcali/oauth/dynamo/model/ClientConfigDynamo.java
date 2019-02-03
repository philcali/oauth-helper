package me.philcali.oauth.dynamo.model;

import com.amazonaws.services.dynamodbv2.document.Item;

import me.philcali.oauth.api.model.IClientConfigData;
import me.philcali.oauth.api.model.IUserClientConfig;

public class ClientConfigDynamo extends IClientConfigData implements IUserClientConfig {
    private String email;

    public ClientConfigDynamo(final Item item) {
        email = item.getString("email");
        setApi(item.getString("api"));
        setClientId(item.getString("clientId"));
        setClientSecret(item.getString("clientSecret"));
        setRedirectUrl(item.getString("redirectUrl"));
        setScopes(item.getList("scopes"));
        setConnectTimeout(item.getInt("connectTimeout"));
        setReadTimeout(item.getInt("readTimeout"));
    }

    @Override
    public String getUserId() {
        return email;
    }
}
