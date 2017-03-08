package eu.intent.sdk.api.internal;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import eu.intent.sdk.auth.internal.Oauth;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * An interceptor to add the headers needed by the API.
 */
public class RetrofitHeadersInterceptor implements Interceptor {
    private static final String TOKEN_ENDPOINT = "token";

    private String mUserAgent;
    private Oauth mOauth;

    public RetrofitHeadersInterceptor(@NonNull Context context, @NonNull Oauth oauth) {
        mOauth = oauth;
        try {
            mUserAgent = getUserAgent(context);
        } catch (PackageManager.NameNotFoundException e) {
            mUserAgent = "";
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        // The only request that does not need Authorization header is the token request
        if (!request.url().toString().endsWith(TOKEN_ENDPOINT)) {
            addAuthorizationHeader(builder);
        }
        addUserAgentHeader(builder);
        request = builder.build();
        return chain.proceed(request);
    }

    private void addAuthorizationHeader(@NonNull Request.Builder requestBuilder) {
        // Use the current access token
        // If the access token is not valid, the call will fail (401) and the Authenticator will be called
        requestBuilder.header("Authorization", "Bearer " + mOauth.getAccessToken());
    }

    private void addUserAgentHeader(@NonNull Request.Builder requestBuilder) {
        if (!TextUtils.isEmpty(mUserAgent)) {
            try {
                requestBuilder.header("User-Agent", URLEncoder.encode(mUserAgent, "ISO-8859-1"));
            } catch (UnsupportedEncodingException ignored) {
                // Don't add the User-Agent
            }
        }
    }

    @NonNull
    private String getUserAgent(@NonNull Context context) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        Resources res = context.getResources();
        String appName = res.getString(packageInfo.applicationInfo.labelRes);
        String appVersion = packageInfo.versionName;
        return TextUtils.join(" ", new String[]{appName, appVersion}).trim();
    }
}
