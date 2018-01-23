package me.philcali.oauth.dynamo;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import me.philcali.oauth.api.ITokenRespository;
import me.philcali.oauth.api.exception.AuthStorageException;
import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.api.model.IExpiringToken;
import me.philcali.oauth.dynamo.model.ExpiringTokenDynamo;

public class TokenRepositoryDynamo implements ITokenRespository {
    private static final long SESSION_EXPIRES = TimeUnit.HOURS.toSeconds(8);
    private final Table tokenTable;

    public TokenRepositoryDynamo(final Table tokenTable) {
        this.tokenTable = tokenTable;
    }

    @Override
    public IExpiringToken generate(final IClientConfig config) {
        Item item = new Item()
                .with("api", config.getApi())
                .with("accessToken", UUID.randomUUID().toString())
                .with("tokenType", "WEBSITE")
                .with("refreshToken", UUID.randomUUID().toString())
                .withLong("expiresIn", now() + SESSION_EXPIRES);
        try {
            tokenTable.putItem(item);
            return new ExpiringTokenDynamo(item);
        } catch (SdkBaseException ase) {
            throw new AuthStorageException(ase);
        }
    }

    @Override
    public Optional<IExpiringToken> get(final String api, final String tokenId) {
        try {
            Optional<Item> item = Optional.ofNullable(tokenTable.getItem(
                    "accessToken", tokenId,
                    "api", api));
            return item.map(ExpiringTokenDynamo::new);
        } catch (SdkBaseException e) {
            throw new AuthStorageException(e);
        }
    }

    private long now() {
        return System.currentTimeMillis() / 1000;
    }
}
