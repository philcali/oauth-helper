package me.philcali.oauth.ssm;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathResult;

import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.api.transfer.ClientConfig;
import me.philcali.oauth.spi.config.IConfigProvider;

public class SystemManagerConfigProvider implements IConfigProvider {
    private static final int MAX_RESULTS = 10;
    private static final Map<String, Function<ClientConfig.Builder, Consumer<String>>> APPLICATORS;
    private final String applicationName;
    private final AWSSimpleSystemsManagement ssm;
    static {
        APPLICATORS = new HashMap<>();
        APPLICATORS.put("client_id", builder -> builder::withClientId);
        APPLICATORS.put("client_secret", builder -> builder::withClientSecret);
        APPLICATORS.put("redirect_url", builder -> builder::withRedirectUrl);
    }

    public SystemManagerConfigProvider(
            final String applicationName,
            final AWSSimpleSystemsManagement ssm) {
        this.applicationName = applicationName;
        this.ssm = ssm;
    }

    @Override
    public IClientConfig getConfig(final String apiType) {
        final String pathPrefix = "/" + applicationName + "/" + apiType + "/";
        final GetParametersByPathResult result = ssm.getParametersByPath(new GetParametersByPathRequest()
                .withPath(pathPrefix)
                .withRecursive(true)
                .withWithDecryption(true)
                .withMaxResults(MAX_RESULTS));
        final ClientConfig.Builder builder = ClientConfig.builder().withApi(apiType);
        result.getParameters().forEach(parameter -> {
            final String name = parameter.getName().replace(pathPrefix, "");
            switch (name) {
            case "scopes":
                builder.withScopes(parameter.getValue().split("\\s*,\\s*"));
                break;
            default:
                APPLICATORS.get(name).apply(builder).accept(parameter.getValue());
            }
        });
        return builder.build();
    }
}
