package eu.intent.sdk.network;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import java.io.IOException;

import eu.intent.sdk.auth.ITOAuthManager;
import eu.intent.sdk.auth.ITOAuthToken;
import eu.intent.sdk.auth.ITSessionManager;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Called when a request fails due to 401 error.
 */
public class ITRetrofitAuthenticator implements Authenticator {
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private ITOAuthManager mOAuthManager;
    private LocalBroadcastManager mLocalBroadcastManager;

    public ITRetrofitAuthenticator(@NonNull Context context, @NonNull ITOAuthManager oauthManager) {

        mOAuthManager = oauthManager;
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        // Refresh the access token synchronously
        ITOAuthToken newToken = mOAuthManager.refreshToken();
        if (newToken != null && !TextUtils.isEmpty(newToken.getAccessToken())) {
            // Remove any existing Authorization header before adding the new one
            return response.request().newBuilder().header(HEADER, PREFIX + newToken.getAccessToken()).build();
        } else {
            // No access token, need to sign in
            Intent sessionExpired = new Intent(ITSessionManager.ACTION_SESSION_EXPIRED);
            mLocalBroadcastManager.sendBroadcast(sessionExpired);
            // Abort
            return null;
        }
    }
}
