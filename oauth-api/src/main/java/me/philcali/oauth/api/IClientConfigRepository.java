package me.philcali.oauth.api;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;

import me.philcali.db.api.Filters;
import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.api.transfer.ClientConfig;

public interface IClientConfigRepository {
    Random RANDOM = new Random();

    default IClientConfig generate(String email, String apiType, String...scopes) {
        final IClientConfig credentials = ClientConfig.builder()
                .withApi(apiType)
                .withClientId(generateSecureHash(256))
                .withClientSecret(generateSecureHash(128))
                .withScopes(scopes)
                .build();
        save(email, credentials);
        return credentials;
    }

    default String generateSecureHash(int size) {
        return new BigInteger(size, RANDOM).toString(32);
    }

    Optional<IClientConfig> get(String api, String clientId);

    QueryResult<IClientConfig> list(QueryParams params);

    default QueryResult<IClientConfig> listByOwners(final String email) {
        return list(QueryParams.builder()
                .withFilters(Filters.attribute("email").equalsTo(email))
                .build());
    }

    void save(String email, IClientConfig config);
}
