package me.philcali.oauth.dynamo.model;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;

import me.philcali.oauth.api.model.IAuthNonce;

public class AuthNonceDynamo implements IAuthNonce {
    private final Item item;

    public AuthNonceDynamo(final Item item) {
        this.item = item;
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
    public String getId() {
        return item.getString("id");
    }

    @Override
    public Map<String, String> getParams() {
        return item.getMap("params");
    }

}
