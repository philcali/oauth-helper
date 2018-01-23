package me.philcali.oauth.api;

import java.util.Optional;

import me.philcali.db.api.Filters;
import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.oauth.api.model.IClientConfig;

public interface IClientConfigRepository {
    Optional<IClientConfig> get(String api, String clientId);

    QueryResult<IClientConfig> list(QueryParams params);

    default QueryResult<IClientConfig> listByOwners(final String email) {
        return list(QueryParams.builder()
                .withFilters(Filters.attribute("email").equalsTo(email))
                .build());
    }

    void save(String email, IClientConfig config);
}
