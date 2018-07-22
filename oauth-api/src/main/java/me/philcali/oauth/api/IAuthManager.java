package me.philcali.oauth.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.StringJoiner;

import me.philcali.oauth.api.exception.AuthException;
import me.philcali.oauth.api.model.IProfile;
import me.philcali.oauth.api.model.IToken;

public interface IAuthManager {
    IToken exchange(String code) throws AuthException;

    default String generateState(String...state) {
        final StringJoiner joiner = new StringJoiner(":");
        Arrays.stream(state).forEach(joiner::add);
        return joiner.toString();
    }

    default String getAuthUrl(String...state) {
        return getAuthUrl(Collections.emptyMap(), state);
    }

    String getAuthUrl(Map<String, String> params, String...state);

    IProfile me(IToken token) throws AuthException;

    default String[] parseState(String state) {
        return state.split(":");
    }
}
