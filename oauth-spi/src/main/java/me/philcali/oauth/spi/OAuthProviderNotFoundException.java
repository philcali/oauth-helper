package me.philcali.oauth.spi;

public class OAuthProviderNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3300567728470910903L;

    public OAuthProviderNotFoundException(final String message) {
        super(message);
    }
}
