package me.philcali.oauth.dynamo;

import java.util.Optional;
import java.util.function.Function;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.db.dynamo.IRetrievalStrategy;
import me.philcali.db.dynamo.QueryRetrievalStrategy;
import me.philcali.oauth.api.IClientConfigRepository;
import me.philcali.oauth.api.exception.AuthStorageException;
import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.api.model.IUserClientConfig;
import me.philcali.oauth.dynamo.model.ClientConfigDynamo;

public class ClientConfigRepositoryDynamo implements IClientConfigRepository {
    private final Table applicationTable;
    private final IRetrievalStrategy query;

    public ClientConfigRepositoryDynamo(final Table applicationTable) {
        this(applicationTable, QueryRetrievalStrategy.fromTable(applicationTable));
    }

    public ClientConfigRepositoryDynamo(final Table applicationTable, final IRetrievalStrategy query) {
        this.applicationTable = applicationTable;
        this.query = query;
    }

    @Override
    public Optional<IUserClientConfig> get(String api, String clientId) {
        try {
            Optional<Item> item = Optional.ofNullable(applicationTable.getItem("clientId", clientId, "api", api));
            return item.map(ClientConfigDynamo::new);
        } catch (SdkBaseException be) {
            throw new AuthStorageException(be);
        }
    }

    @Override
    public QueryResult<IUserClientConfig> list(final QueryParams params) {
        final Function<Item, IUserClientConfig> thunk = ClientConfigDynamo::new;
        return query.andThen(result -> result.map(thunk)).apply(params, applicationTable);
    }

    @Override
    public IUserClientConfig save(String email, IClientConfig config) {
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
            return new ClientConfigDynamo(item);
        } catch (SdkBaseException ase) {
            throw new AuthStorageException(ase);
        }
    }
}
