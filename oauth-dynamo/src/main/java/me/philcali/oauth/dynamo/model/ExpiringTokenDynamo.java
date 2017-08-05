package me.philcali.oauth.dynamo.model;

import com.amazonaws.services.dynamodbv2.document.Item;

import me.philcali.oauth.api.IExpiringToken;

public class ExpiringTokenDynamo implements IExpiringToken {
    private final Item item;

    public ExpiringTokenDynamo(Item item) {
        this.item = item;
    }

    @Override
    public String getAccessToken() {
        return item.getString("accessToken");
    }

    @Override
    public String getApi() {
        return item.getString("api");
    }

    @Override
    public long getExpiresIn() {
        return item.getLong("expiresIn");
    }

    @Override
    public String getRefreshToken() {
        return item.getString("refreshToken");
    }

    @Override
    public String getTokenType() {
        return item.getString("tokenType");
    }

}
