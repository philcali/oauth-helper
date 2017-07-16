package me.philcali.oauth.api.exception;

public class AuthException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -759334627542066623L;

    public AuthException(Throwable t) {
        super(t);
    }
}
