package eu.intent.sdk.api.internal;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;

import eu.intent.sdk.auth.internal.Oauth;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An interceptor to add the headers needed by the API.
 */
public class RetrofitHeadersInterceptor implements Interceptor {
    private static final String TOKEN_ENDPOINT = "token";

    private Context mContext;
    private String mUserAgent;

    public RetrofitHeadersInterceptor(@NonNull Context context) {
        mContext = context;
        mUserAgent = getUserAgent(context);
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
        Oauth oauth = Oauth.getInstance(mContext);
        requestBuilder.header("Authorization", "Bearer " + oauth.getAccessToken());
    }

    private void addUserAgentHeader(@NonNull Request.Builder requestBuilder) {
        if (!TextUtils.isEmpty(mUserAgent)) {
            requestBuilder.header("User-Agent", mUserAgent);
        }
    }

    @NonNull
    private String getUserAgent(@NonNull Context context) {
        String appName = context.getString(context.getApplicationInfo().labelRes);
        String appVersion;
        try {
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException ignored) {
            appVersion = "";
        }
        return TextUtils.join(" ", new String[]{appName, appVersion}).trim();
    }
}
