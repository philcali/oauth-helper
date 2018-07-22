package me.philcali.oauth.google;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.philcali.http.api.HttpMethod;
import me.philcali.http.api.IHttpClient;
import me.philcali.http.api.IRequest;
import me.philcali.http.api.IResponse;
import me.philcali.http.api.exception.HttpException;
import me.philcali.http.api.util.URLBuilder;
import me.philcali.http.java.NativeClientConfig;
import me.philcali.http.java.NativeHttpClient;
import me.philcali.oauth.api.IExpiringAuthManager;
import me.philcali.oauth.api.exception.AuthException;
import me.philcali.oauth.api.model.IClientConfig;
import me.philcali.oauth.api.model.IExpiringToken;
import me.philcali.oauth.api.model.IProfile;
import me.philcali.oauth.api.model.IToken;

public class AuthManagerGoogle implements IExpiringAuthManager {
    private static final String AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
    private static final String TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
    private static final String PROFILE_URL = "https://www.googleapis.com/userinfo/v2/me";

    private final IClientConfig config;
    private final IHttpClient client;
    // TODO: possibly extract this
    private final ObjectMapper mapper;

    public AuthManagerGoogle(final IClientConfig config) {
        this(config, new NativeHttpClient(new NativeClientConfig()
                .withConnectTimeout(config.getConnectTimeout())
                .withRequestTimeout(config.getReadTimeout())), new ObjectMapper());
    }

    public AuthManagerGoogle(final IClientConfig config, final IHttpClient client, final ObjectMapper mapper) {
        this.config = config;
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IExpiringToken exchange(String code) {
        IRequest call = client.createRequest(HttpMethod.POST, TOKEN_URL)
                .post("code", code)
                .post("client_id", config.getClientId())
                .post("client_secret", config.getClientSecret())
                .post("redirect_uri", config.getRedirectUrl())
                .post("grant_type", "authorization_code");
        try {
            IResponse response = call.respond();
            return new AuthToken(mapper.readValue(response.body(), HashMap.class));
        } catch (IOException | HttpException e) {
            throw new AuthException(e);
        }
    }

    @Override
    public String getAuthUrl(final Map<String, String> additionalParams, final String...state) {
        final StringJoiner joiner = new StringJoiner(" ");
        config.getScopes().forEach(joiner::add);
        final URLBuilder builder = new URLBuilder(AUTH_URL)
                .addQueryParam("client_id", config.getClientId())
                .addQueryParam("redirect_uri", config.getRedirectUrl())
                .addQueryParam("response_type", "code")
                .addQueryParam("scope", joiner.toString())
                .addQueryParam("state", generateState(state))
                .addQueryParam("include_granted_scopes", true);
        additionalParams.forEach(builder::addQueryParam);
        return builder.build().toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public IProfile me(IToken token) {
        IRequest call = client.createRequest(HttpMethod.GET, PROFILE_URL)
                .header("Authorization", String.format("Bearer %s", token.getAccessToken()));
        try {
            IResponse response = call.respond();
            return new Profile(mapper.readValue(response.body(), HashMap.class));
        } catch (IOException | HttpException e) {
            throw new AuthException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public IExpiringToken refresh(String token) {
        IRequest call = client.createRequest(HttpMethod.POST, TOKEN_URL)
                .post("refresh_token", token)
                .post("client_id", config.getClientId())
                .post("client_secret", config.getClientSecret())
                .post("grant_type", "refresh_token");
        try {
            IResponse response = call.respond();
            return new AuthToken(token, config.getClientId(),
                    mapper.readValue(response.body(), HashMap.class));
        } catch (IOException | HttpException e) {
            throw new AuthException(e);
        }
    }
}
