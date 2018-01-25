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

### OAuth SPI (Service Provider Interface)

The `oauth-spi` package will bring in the `OAuthProvider` SPI, which will
allow users to get / create generic `IAuthManager`s at will. For example:

```
IAuthManager googleLogins = OAuthProviders.getAuthManager(ClientConfig.builder()
        .withApiType("google")
        .withClientId(googleClientId)
        .withClientSecret(googleClientSecret)
        .withRedirectUrl(googleRedirectUrl)
        .build());

IAuthManager slackLogins = OAuthProviders.getAuthManager(ClientConfig.builder()
        .withApiType("slack")
        .withClientId(googleClientId)
        .withClientSecret(googleClientSecret)
        .build());

// Shopify requires a shop
IAuthManager shopifyLogins = OAuthProviders.newAuthManager(ClientConfig.builder()
        .withApiType("shopify")
        .withClientId(googleClientId)
        .withClientSecret(googleClientSecret)
        .build(), "my.shop.com");
```

### Google Implementation

```
IExpiringAuthManager authManager = new AuthManagerGoogle(ClientConfig.builder()
        .withClientId(clientId)
        .withClientSecret(clientSecret)
        .withRedirectUrl(redirectUrl)
        .build());
```

### Slack Implementation

The [slack-helper][1] comes with an implementation of the auth manager

```
IAuthManager authManager = new SlackIntegrationImpl(ClientConfig.builder()
        .withClientId(clientId)
        .withClientSecret(clientSecret)
        .build());
```

### Shopify Implementation

The [shopify-helper][2] comes with an implementation of the auth manager.
Since the OAuth interaction is performed on a shop by shop basis, use
the `IShopifyIntegration` to generate shop specific auth managers

```
IAuthManager authManager = new ShopifyIntegrationImpl(ClientConfig.builder()
        .withClientId(clientId)
        .withClientSecret(clientSecret)
        .build())
        .createAuth(shop);
```

[1]: https://github.com/philcali/slack-helper
[2]: https://github.com/philcali/shopify-helper
