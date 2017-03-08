package eu.intent.sdk.api.internal;

import android.content.Context;
import android.support.annotation.NonNull;

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

    private Oauth mOauth;

    public RetrofitHeadersInterceptor(@NonNull Oauth oauth) {
        mOauth = oauth;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        // The only request that does not need Authorization header is the token request
        if (!request.url().toString().endsWith(TOKEN_ENDPOINT)) {
            addAuthorizationHeader(builder);
        }
        request = builder.build();
        return chain.proceed(request);
    }

    private void addAuthorizationHeader(@NonNull Request.Builder requestBuilder) {
        // Use the current access token
        // If the access token is not valid, the call will fail (401) and the Authenticator will be called
        requestBuilder.header("Authorization", "Bearer " + mOauth.getAccessToken());
    }
}
