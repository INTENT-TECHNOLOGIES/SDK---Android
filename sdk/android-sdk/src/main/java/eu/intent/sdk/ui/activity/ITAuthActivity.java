package eu.intent.sdk.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import eu.intent.sdk.R;
import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITApiError;
import eu.intent.sdk.auth.ITClient;
import eu.intent.sdk.auth.ITOAuthApi;
import eu.intent.sdk.auth.ITOAuthManager;
import eu.intent.sdk.auth.ITOAuthToken;
import eu.intent.sdk.auth.ITSessionManager;
import eu.intent.sdk.network.ITRetrofitHelper;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * This is an authentication activity, displaying a login form in a web view.
 */
public final class ITAuthActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_CLIENT = "EXTRA_CLIENT";

    private WebView mWebView;
    private View mNoInternetView;

    private ITSessionManager mSessionManager;
    private ITClient mClient;
    private ITOAuthManager mOAuthManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.it_activity_auth);

        mWebView = (WebView) findViewById(R.id.it_auth_webview);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String code = Uri.parse(url).getQueryParameter("code");
                requestToken(code);
                return false;
            }
        });
        mNoInternetView = findViewById(R.id.it_auth_layout_no_internet);

        mSessionManager = new ITSessionManager(this);
        mClient = getIntent().getParcelableExtra(EXTRA_CLIENT);

        Retrofit oauthRetrofit = ITRetrofitHelper.INSTANCE.getDefaultAuthRetrofit(this, mClient);
        ITOAuthApi oauthApi = oauthRetrofit.create(ITOAuthApi.class);
        mOAuthManager = new ITOAuthManager(this, mClient, oauthApi);

        tryLogIn();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.it_auth_button_retry) {
            tryLogIn();
        }
    }

    private void tryLogIn() {
        mNoInternetView.setVisibility(View.INVISIBLE);
        mWebView.setVisibility(View.VISIBLE);
        if (mSessionManager.isLoggedIn()) {
            // Access token is valid: no need to authenticate
            onAuthenticationSuccess();
        } else {
            // Check connectivity
            NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                if (mSessionManager.canRefreshToken()) {
                    // Use the refresh token to refresh the access token
                    refreshToken();
                } else {
                    // No token available, load the web form
                    loadWebForm();
                }
            } else {
                mWebView.setVisibility(View.INVISIBLE);
                mNoInternetView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadWebForm() {
        mWebView.loadUrl(mClient.getWebFormUrl());
    }

    private void requestToken(String code) {
        mOAuthManager.requestToken(code, new ITApiCallback<ITOAuthToken>() {
            @Override
            public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                String text = (body.getHttpCode() > 0 ? "HTTP " + body.getHttpCode() + ": " : "")
                        + (TextUtils.isEmpty(body.getMessage()) ? body.getHttpMessage() : body.getMessage());
                Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show();
                loadWebForm();
            }

            @Override
            public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                onAuthenticationSuccess();
            }
        });
    }

    private void refreshToken() {
        mOAuthManager.refreshToken(new ITApiCallback<ITOAuthToken>() {
            @Override
            public void onFailure(@Nullable Call<ITOAuthToken> call, @NotNull ITApiError body) {
                // The user needs to sign in: load the web form
                loadWebForm();
            }

            @Override
            public void onSuccess(@Nullable Call<ITOAuthToken> call, ITOAuthToken body) {
                onAuthenticationSuccess();
            }
        });
    }

    private void onAuthenticationSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
