package eu.intent.sdk.api.internal;

import android.content.Context;

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

    public RetrofitHeadersInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // The only request that does not need headers is the token request
        if (!request.url().toString().endsWith(TOKEN_ENDPOINT)) {
            // Use the current access token
            // If the access token is not valid, the call will fail (401) and the Authenticator will be called
            Oauth oauth = Oauth.getInstance(mContext);
            request = request.newBuilder().header("Authorization", "Bearer " + oauth.getAccessToken()).build();
        }
        return chain.proceed(request);
    }
}
