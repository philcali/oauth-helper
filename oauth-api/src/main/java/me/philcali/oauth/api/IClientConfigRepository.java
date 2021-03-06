package me.philcali.oauth.api;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;

import me.philcali.db.api.Conditions;
import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.api.model.IClientConfigData;
import me.philcali.oauth.api.model.IUserClientConfig;

public interface IClientConfigRepository {
    Random RANDOM = new Random();

    default IUserClientConfig generate(String email, String apiType, String...scopes) {
        final IClientConfig credentials = IClientConfigData.builder()
                .withApi(apiType)
                .withClientId(generateSecureHash(256))
                .withClientSecret(generateSecureHash(128))
                .addScopes(scopes)
                .build();
        return save(email, credentials);
    }

    default String generateSecureHash(int size) {
        return new BigInteger(size, RANDOM).toString(32);
    }

    Optional<IUserClientConfig> get(String api, String clientId);

    QueryResult<IUserClientConfig> list(QueryParams params);

    default QueryResult<IUserClientConfig> listByOwners(final String email) {
        return list(QueryParams.builder()
                .withConditions(Conditions.attribute("email").equalsTo(email))
                .build());
    }

    default Optional<IUserClientConfig> getByOwner(final String email, final String api) {
        return list(QueryParams.builder()
                .withConditions(
                        Conditions.attribute("email").equalsTo(email),
                        Conditions.attribute("api").equalsTo(api))
                .build())
                .getItems().stream()
                .findFirst();
    }

    IUserClientConfig save(String email, IClientConfig config);
}
