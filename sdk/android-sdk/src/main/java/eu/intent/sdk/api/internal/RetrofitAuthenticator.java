package eu.intent.sdk.api.internal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import java.io.IOException;

import eu.intent.sdk.auth.internal.Oauth;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Called when a request fails due to 401 error.
 */
public class RetrofitAuthenticator implements Authenticator {
    private Oauth mOauth;
    private LocalBroadcastManager mLocalBroadcastManager;

    public RetrofitAuthenticator(@NonNull Context context, @NonNull Oauth oauth) {
        mOauth = oauth;
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        // Refresh the access token synchronously
        String accessToken = mOauth.refreshToken();
        if (!TextUtils.isEmpty(accessToken)) {
            // Remove any existing Authorization header before adding the new one
            return response.request().newBuilder().header("Authorization", "Bearer " + accessToken).build();
        } else {
            // No access token, need to sign in
            Intent sessionExpired = new Intent(Oauth.ACTION_SESSION_EXPIRED);
            mLocalBroadcastManager.sendBroadcast(sessionExpired);
            // Abort
            return null;
        }
    }
}
