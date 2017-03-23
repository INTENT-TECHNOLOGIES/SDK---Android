Intent Android SDK
------------------

This SDK makes it easier to call the Intent API in your Android apps.

Setup
-----

Add the SDK to the dependencies of your Gradle module:

```
dependencies {
    compile 'eu.intent:android-sdk:2.0'
}
```

Authentication
--------------

1. Create an instance of `ITClient`:

```
ITClient intentClient = new ITClient(
  // The environment, PRODUCTION or SANDBOX
  ITEnvironment.PRODUCTION,
  // Your authentication credentials
  <clientId>,
  <clientSecret>,
  <clientRedirectUri>,
  // The scopes needed by your app
  <scope1>, <scope2>,...
); 
```

2. Declare the login Activity in your `AndroidManifest.xml`:

```
<activity
  android:name="eu.intent.sdk.ui.activity.ITAuthActivity"
  android:theme="@style/Theme.AppCompat.NoActionBar" />
```

3. Start the login Activity from one of your own Activities (typically your Launcher Activity):

```
public class YourActivity extends AppCompatActivity{
  private static final int REQUEST_AUTH = 0;

  ...

    // Somewhere in your Activity lifecycle
    ITAuthHelper.INSTANCE.openLoginActivity(<parentActivity>, intentClient, REQUEST_AUTH);

  ...

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_AUTH && resultCode == Activity.RESULT_OK) {
      // Authentication succeeded, start your main Activity or whatever you want
      ...
    }
    // You should finish this Activity to prevent navigating back
    finish();
  }
}
```

Handling the session
--------------------

The util class `ITSessionManager` gives you access to the token of the current session:

```
ITSessionManager sessionManager = new ITSessionManager(context);
sessionManager.getAccessToken();        // The access token for the authenticated user
sessionManager.getAccessTokenExpiry();  // The expiry timestamp of the current user session
sessionManager.isLoggedIn();            // True if a user has logged in and the session has not expired
sessionManager.logout();                // Clears the current session
```

For example:

```
if (sessionManager.isLoggedIn()) {
  // You can call the Intent API
} else {
  // Ask the user to log in again
}
```

Refresh token
-------------

You can also refresh your user's sessions if you have the right to use a refresh token:

1. Create an instance of the Intent OAuth manager:

```
Retrofit oauthRetrofit = ITRetrofitHelper.getDefaultAuthRetrofit(context, intentClient);
ITOAuthApi oauthApi = oauthRetrofit.create(ITOAuthApi.class);
ITOAuthManager oauthManager = new ITOAuthManager(context, intentClient, oauthApi);
```

2. Refresh the session token:

```
if (sessionManager.isLoggedIn()) {
  // You can call the Intent API
} else if (sessionManager.canRefreshToken()) {
  // Refresh the token
  oauthManager.refreshToken(new ITApiCallback<ITOAuthToken>() {
    @Override
    public void onSuccess(Call<ITOAuthToken> call, ITOAuthToken body) {
      // The new token is automatically saved in the session, you can call the Intent API
    }

    @Override
    public void onFailure(Call<ITOAuthToken> call, ITApiError body) {
      // Refresh failed, ask the user to log in again
    }
  });
} else {
  // Ask the user to log in again
}
```

Calling the API
---------------

You must add an `Authorization` header to your calls to the Intent API:

```
String authorizationHeader = "Bearer " + sessionManager.getAccessToken();
// Call the Intent API with the Authorization header
```

Retrofit
--------

The Intent Android SDK is compliant with the [Retrofit](http://square.github.io/retrofit/) REST library, which can save you some of the above code, especially about OAuth:

### HTTP client

The SDK provides the following Retrofit interceptors:

- `ITRetrofitAuthorizationInterceptor` intercepts the requests to add the `Authorization` header with the access token.
- `ITRetrofitAuthenticator` is used when a request asks for authentication: if possible, the authenticator refreshes the token and sends the request with the new access token; otherwise, it broadcasts an Intent with the action `ITSessionManager.ACTION_SESSION_EXPIRED`.
- `ITRetrofitGzipInterceptor` intercepts the requests to zip their body.

You can use them when building your own instance of `OkHttpClient`, or use the default `Builder` that you can customize:

```
OkHttpClient.Builder httpClientBuilder = ITRetrofitHelper.getDefaultApiHttpClientBuilder(context, intentClient);
```

### Gson

The SDK provides model classes for most of the objects you can handle via the Intent API. These classes provide `JsonDeserializers` that are used to parse the JSON returned by the Intent API. You can build your instance of `Gson` from the default `GsonBuilder` that you can customize with your own type adapters:

```
GsonBuilder gsonBuilder = ITRetrofitHelper.getDefaultGsonBuilder();
```

### Retrofit services

Once you have created your instance of `OkHttpClient` and `Gson`, you can build an instance of `Retrofit`:

```
Retrofit retrofit = ITRetrofitHelper.getRetrofitApiBuilder(intentClient, httpClientBuilder.build(), gsonBuilder.create()).build();
```

It will use the right base URL depending on the `ITEnvironment` you passed as a parameter of your `ITClient`.

If you don't need to create a custom `OkHttpClient` or `Gson`, you can simply create a default `Retrofit` instance:

```
Retrofit retrofit = ITRetrofitHelper.getDefaultApiRetrofit(context, intentClient);
```

You can then create instances of the Intent APIs. Each one declares methods that match most of the routes you can find in the [API reference](https://api.hubintent.com/documentation/reference.html):

```
ITAccountApi accountApi = retrofit.create(ITAccountApi.class);
accountApi.me().enqueue(new ITApiCallback() {
    @Override
    public void onSuccess(Call<ITUser> call, ITUser result) {
      ...
    }
    @Override
    public void onFailure(Call<ITUser> call, ITApiError apiError) {
      ...
    }
  });
```

Single instances
----------------

It should be a good practice to provide your `ITApiClient`, `ITSessionManager`, `Retrofit` and `ITOAuthManager` (if you need one) as _single_ instances to avoid creating the same object several times. You can use your favorite dependency injection framework, or take a look at the Sample app to get an idea of how to do it.

