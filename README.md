Intent Android SDK
------------------

This SDK makes it easier to call the Intent API in your Android apps.

Usage
-----

### Setup

Add the SDK to the dependencies of your Gradle module:

```
dependencies {
    compile 'eu.intent:android-sdk:1.0.2'
}
```

### Application

Create a configuration file in `res/raw/`, in which you store your client credentials:

```
it_client_id            =   <YOUR_CLIENT_ID>
it_client_secret        =   <YOUR_CLIENT_SECRET>
it_auth_redirect_url    =   <YOUR_REDIRECT_URL>
```

Your `Application` class must extend `ITApp`, where you declare your configuration file:

```
public class App extends ITApp {
    @Override
    public int getConfigFileResId() {
        return R.raw.config;
    }
}
```

Don't forget to declare your class in `AndroidManifest.xml`:

```
<application android:name=".App">
    ...
</application>
```

### Authentication

Declare the authentication activity in `AndroidManifest.xml`:

```
<activity android:name="eu.intent.sdk.ui.activity.ITAuthActivity" />
```
and start it from an `Activity` of yours:

```
public class StartupActivity extends AppCompatActivity {
    private static final int REQUEST_AUTH = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Declare the scopes you need in your app
        ITAuthRequest authRequest = new ITAuthRequest.Builder(this).addScopes("read:me", "read:datahub:parts").build();
        ITAuthClient.openLoginActivity(this, REQUEST_AUTH, authRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_AUTH && resultCode == RESULT_OK) {
            // Authentication succeeded
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
```

You can log out by calling `ITAuthClient.logout(Context);`.

### API

The API can be requested by calling methods wrapped in the relevant classes. Each API method takes an `ITApiCallback` parameter, where you can handle the result:

```
ITUserApi api = new ITUserApi(context);
api.getCurrentUser(new ITApiCallback<ITUser>() {
    @Override
    public void onSuccess(ITUser result) {
    	// This is executed in the UI thread
        myTextView.setText(result.getFullName());
    }

    @Override
    public void onFailure(int httpCode, String message) {
        // D'oh! :(
    }
});
```

Sample app
----------

You can import the source code in the `sample` directory, use your client credentials in `res/raw/config` and run the app.
