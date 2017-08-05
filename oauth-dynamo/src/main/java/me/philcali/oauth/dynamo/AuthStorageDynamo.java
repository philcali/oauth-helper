package me.philcali.oauth.dynamo;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import me.philcali.oauth.api.ClientConfig;
import me.philcali.oauth.api.IAuthStorage;
import me.philcali.oauth.api.IExpiringToken;
import me.philcali.oauth.api.exception.AuthStorageException;
import me.philcali.oauth.api.model.IAuthNonce;
import me.philcali.oauth.dynamo.model.AuthNonceDynamo;
import me.philcali.oauth.dynamo.model.ClientConfigDynamo;
import me.philcali.oauth.dynamo.model.ExpiringTokenDynamo;

public class AuthStorageDynamo implements IAuthStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthStorageDynamo.class);
    private static final long NONCE_EXPIRES = TimeUnit.MINUTES.toSeconds(15);
    private static final long SESSION_EXPIRES = TimeUnit.HOURS.toSeconds(8);
    private final Table nonceTable;
    private final Table applicationTable;
    private final Table tokenTable;
    private final Index applicationIndex;

    public AuthStorageDynamo(final DynamoDB db, final AuthStorageDynamoConfig config) {
        this.nonceTable = db.getTable(config.getNonceTableName());
        this.applicationTable = db.getTable(config.getApplicationTableName());
        this.tokenTable = db.getTable(config.getTokenTableName());
        this.applicationIndex = applicationTable.getIndex("email-index");
    }

    private boolean delete(IAuthNonce nonce) {
        boolean success = true;
        try {
            nonceTable.deleteItem("id", nonce.getId(), "api", nonce.getApi());
        } catch (AmazonServiceException ase) {
            LOGGER.error("Failed to delete nonce {} - {}", nonce.getApi(), nonce.getId(), ase);
            success = false;
        }
        return success;
    }

    @Override
    public IAuthNonce generateAuthNonce(String api, Map<String, String> params) {
        Item item = new Item()
                .with("api", api)
                .with("id", UUID.randomUUID().toString())
                .withLong("expiresIn", now() + NONCE_EXPIRES);
        Optional.ofNullable(params).filter(p -> !p.isEmpty()).ifPresent(p -> item.withMap("params", p));
        try {
            nonceTable.putItem(item);
            return new AuthNonceDynamo(item);
        } catch (AmazonServiceException ase) {
            throw new AuthStorageException(ase);
        }
    }

    @Override
    public IExpiringToken generateSessionToken(ClientConfig config) {
        Item item = new Item()
                .with("api", config.getApi())
                .with("accessToken", UUID.randomUUID().toString())
                .with("tokenType", "WEBSITE")
                .with("refreshToken", UUID.randomUUID().toString())
                .withLong("expiresIn", now() + SESSION_EXPIRES);
        try {
            tokenTable.putItem(item);
            return new ExpiringTokenDynamo(item);
        } catch (AmazonServiceException ase) {
            throw new AuthStorageException(ase);
        }
    }

    @Override
    public Optional<ClientConfig> getConfig(String api, String clientId) {
        Optional<Item> item = Optional.ofNullable(applicationTable.getItem("clientId", clientId, "api", api));
        return item.map(ClientConfigDynamo::new);
    }

    @Override
    public Stream<ClientConfig> getConfigsForOwner(String email) {
        return StreamSupport.stream(applicationIndex.query("email", email).spliterator(), false)
                .map(ClientConfigDynamo::new);
    }

    @Override
    public Optional<IExpiringToken> getSession(String api, String tokenId) {
        Optional<Item> item = Optional.ofNullable(tokenTable.getItem(
                "accessToken", tokenId,
                "api", api));
        return item.map(ExpiringTokenDynamo::new);
    }

    private long now() {
        return System.currentTimeMillis() / 1000;
    }

    @Override
    public Optional<ClientConfig> save(String email, ClientConfig config) {
        Item item = new Item()
                .with("email", email)
                .with("api", config.getApi())
                .with("clientId", config.getClientId())
                .with("clientSecret", config.getClientSecret())
                .with("redirectUrl", config.getRedirectUrl())
                .withList("scopes", config.getScopes())
                .withInt("connectTimeout", config.getConnectTimeout())
                .withInt("readTimeout", config.getReadTimeout());
        try {
            applicationTable.putItem(item);
            return Optional.of(config);
        } catch (AmazonServiceException ase) {
            LOGGER.error("Failed to put new application {} - {}", config.getApi(), config.getClientId(), ase);
            return Optional.empty();
        }
    }

    @Override
    public Optional<IAuthNonce> verifyNonce(String id, String api, long timestamp) {
        Optional<IAuthNonce> nonce = Optional.ofNullable(nonceTable.getItem("id", id, "api", api))
                .map(AuthNonceDynamo::new);
        return nonce.filter(this::delete).filter(n -> n.getExpiresIn() > (timestamp / 1000));
    }
}
