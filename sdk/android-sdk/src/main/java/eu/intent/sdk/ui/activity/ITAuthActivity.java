package eu.intent.sdk.ui.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import eu.intent.sdk.R;
import eu.intent.sdk.auth.internal.Oauth;

/**
 * This is an authentication activity, displaying a login form in a web view. You must provide your API client ID and client secret by implementing the abstract methods.
 */
public final class ITAuthActivity extends AppCompatActivity {
    public static final String EXTRA_WEB_FORM_URL = "EXTRA_WEB_FORM_URL";

    private Oauth mOauth;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.it_activity_auth);

        mOauth = Oauth.getInstance(this);

        mWebView = (WebView) findViewById(R.id.it_auth_webview);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String code = Uri.parse(url).getQueryParameter("code");
                requestToken(code);
                return false;
            }
        });

        if (mOauth.hasValidAccessToken()) {
            // Access token is valid: no need to authenticate
            onAuthenticationSuccess();
        } else if (!TextUtils.isEmpty(mOauth.getRefreshToken())) {
            // Use the refresh token to refresh the access token
            refreshToken();
        } else {
            // No token available, load the web form
            loadWebForm();
        }
    }

    private void loadWebForm() {
        String webFormUrl = getIntent().getStringExtra(EXTRA_WEB_FORM_URL);
        Log.d(getClass().getCanonicalName(), "Loading " + webFormUrl);
        mWebView.loadUrl(webFormUrl);
    }

    private void requestToken(String code) {
        mOauth.requestToken(code, new Oauth.Callback() {
            @Override
            public void onSuccess(Oauth.Info info) {
                onAuthenticationSuccess();
            }

            @Override
            public void onFailure(int httpCode, String message) {
                String text = (httpCode > 0 ? "HTTP " + httpCode + " " : "") + message;
                Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show();
                loadWebForm();
            }
        });
    }

    private void refreshToken() {
        mOauth.refreshToken(new Oauth.Callback() {
            @Override
            public void onSuccess(Oauth.Info info) {
                onAuthenticationSuccess();
            }

            @Override
            public void onFailure(int httpCode, String message) {
                Log.w(getClass().getCanonicalName(), "Failed to refresh token (HTTP " + httpCode + "): " + message);
                // The user needs to sign in: load the web form
                loadWebForm();
            }
        });
    }

    private void onAuthenticationSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
