package me.philcali.oauth.api;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import me.philcali.oauth.api.model.IAuthNonce;

public interface INonceRepository {
    default IAuthNonce generate(String api) {
        return generate(api, Collections.emptyMap());
    }

    IAuthNonce generate(String api, Map<String, String> params);

    default Optional<IAuthNonce> verify(String id, String api) {
        return verify(id, api, System.currentTimeMillis());
    }

    Optional<IAuthNonce> verify(String id, String api, long timestamp);
}
