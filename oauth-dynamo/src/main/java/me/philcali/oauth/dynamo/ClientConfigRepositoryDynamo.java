package me.philcali.oauth.dynamo;

import java.util.Optional;
import java.util.function.Function;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.db.dynamo.QueryAdapter;
import me.philcali.oauth.api.IClientConfigRepository;
import me.philcali.oauth.api.exception.AuthStorageException;
import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.dynamo.model.ClientConfigDynamo;

public class ClientConfigRepositoryDynamo implements IClientConfigRepository {
    private final String EMAIL_INDEX = "email-index";
    private final Table applicationTable;

    public ClientConfigRepositoryDynamo(final Table applicationTable) {
        this.applicationTable = applicationTable;
    }

    @Override
    public Optional<IClientConfig> get(String api, String clientId) {
        try {
            Optional<Item> item = Optional.ofNullable(applicationTable.getItem("clientId", clientId, "api", api));
            return item.map(ClientConfigDynamo::new);
        } catch (SdkBaseException be) {
            throw new AuthStorageException(be);
        }
    }

    @Override
    public QueryResult<IClientConfig> list(final QueryParams params) {
        final Function<Table, QueryResult<Item>> adapter = QueryAdapter.builder()
                .withHashKey("clientId")
                .withIndexMap("email", applicationTable.getIndex(EMAIL_INDEX))
                .withQueryParams(params)
                .build();
        final Function<Item, IClientConfig> thunk = ClientConfigDynamo::new;
        return adapter.andThen(result -> result.map(thunk)).apply(applicationTable);
    }

    @Override
    public void save(String email, IClientConfig config) {
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
        } catch (SdkBaseException ase) {
            throw new AuthStorageException(ase);
        }
    }
}
