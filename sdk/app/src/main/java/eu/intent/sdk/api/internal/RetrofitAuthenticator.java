package eu.intent.sdk.api.internal;

import android.content.Context;
import android.content.Intent;
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
    private Context mContext;

    public RetrofitAuthenticator(Context context) {
        mContext = context;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        // Refresh the access token synchronously
        String accessToken = Oauth.getInstance(mContext).refreshToken();
        if (!TextUtils.isEmpty(accessToken)) {
            // Remove any existing Authorization header before adding the new one
            return response.request().newBuilder().removeHeader("Authorization").addHeader("Authorization", "Bearer " + accessToken).build();
        } else {
            // No access token, need to sign in
            Intent sessionExpired = new Intent(Oauth.ACTION_SESSION_EXPIRED);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(sessionExpired);
            // Abort
            return null;
        }
    }
}
