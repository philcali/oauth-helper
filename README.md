# OAuth helper

This is a colleciton of _no fuss_, light-weight OAuth
implemetations which can be used on any environment.

## Auth Managers

```
IAuthManager authManager = ...
// Get an authorization URL
String authUrl = authManager.getAuthUrl(/* optional state nonces */);
// Exchange for an authorized token
IExpiringToken token = authManager.exchange(code);
// Refresh when the time comes:
IExpiringToken refreshedToken = authManager.refresh(token);
```

### Google Implementation

```
IExpiringAuthManager authManager = new AuthManagerGoogle(new ClientConfig()
        .withClientId(clientId)
        .withClientSecret(clientSecret)
        .withRedirectUrl(redirectUrl));
```

### Slack Implementation

The slack-helper comes with an implementation of the auth manager

```
IAuthManager authManager = new SlackIntegrationImpl(new ClientConfig()
        .withClientId(clientId)
        .withClientSecret(clientSecret));
```

### Shopify Implementation

The shopify-helper comes with an implementation of the auth manager.
Since the OAuth interaction is performed on a shop by shop basis, use
the `IShopifyIntegration` to generate shop specific auth managers

```

IAuthManager authManager = new ShopifyIntegrationImpl(new ClientConfig()
        .withClientId(clientId)
        .withClientSecret(clientSecret))
        .createAuth(shop);
```
