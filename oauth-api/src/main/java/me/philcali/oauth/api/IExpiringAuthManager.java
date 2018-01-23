package me.philcali.oauth.api;

import me.philcali.oauth.api.exception.AuthException;
import me.philcali.oauth.api.model.IExpiringToken;

public interface IExpiringAuthManager extends IAuthManager {
    @Override
    IExpiringToken exchange(String code) throws AuthException;
    IExpiringToken refresh(String token) throws AuthException;
}
