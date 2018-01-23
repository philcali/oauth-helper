package me.philcali.oauth.dynamo;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import me.philcali.oauth.api.INonceRepository;
import me.philcali.oauth.api.exception.AuthStorageException;
import me.philcali.oauth.api.model.IAuthNonce;
import me.philcali.oauth.dynamo.model.AuthNonceDynamo;

public class NonceRepositoryDynamo implements INonceRepository {
    private static final long NONCE_EXPIRES = TimeUnit.MINUTES.toSeconds(15);
    private final Table nonces;

    public NonceRepositoryDynamo(final Table nonces) {
        this.nonces = nonces;
    }

    private boolean delete(IAuthNonce nonce) {
        boolean success = true;
        try {
            nonces.deleteItem("id", nonce.getId(), "api", nonce.getApi());
        } catch (SdkBaseException ase) {
            success = false;
        }
        return success;
    }

    @Override
    public IAuthNonce generate(String api, Map<String, String> params) {
        Item item = new Item()
                .with("api", api)
                .with("id", UUID.randomUUID().toString())
                .withLong("expiresIn", now() + NONCE_EXPIRES);
        Optional.ofNullable(params).filter(p -> !p.isEmpty()).ifPresent(p -> item.withMap("params", p));
        try {
            nonces.putItem(item);
            return new AuthNonceDynamo(item);
        } catch (SdkBaseException ase) {
            throw new AuthStorageException(ase);
        }
    }

    private final long now() {
        return System.currentTimeMillis() / 1000;
    }

    @Override
    public Optional<IAuthNonce> verify(String id, String api, long timestamp) {
        try {
            Optional<IAuthNonce> nonce = Optional.ofNullable(nonces.getItem("id", id, "api", api))
                    .map(AuthNonceDynamo::new);
            return nonce.filter(this::delete).filter(n -> n.getExpiresIn() > (timestamp / 1000));
        } catch (SdkBaseException e) {
            throw new AuthStorageException(e);
        }
    }
}
