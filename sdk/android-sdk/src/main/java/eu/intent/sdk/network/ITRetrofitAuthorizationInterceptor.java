package eu.intent.sdk.network;

import android.support.annotation.NonNull;

import java.io.IOException;

import eu.intent.sdk.auth.ITSessionManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * An interceptor to add the Authorization header needed by the API.
 */
public class ITRetrofitAuthorizationInterceptor implements Interceptor {
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private ITSessionManager mSessionManager;

    public ITRetrofitAuthorizationInterceptor(@NonNull ITSessionManager sessionManager) {
        mSessionManager = sessionManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .header(HEADER, PREFIX + mSessionManager.getAccessToken())
                .build();
        return chain.proceed(request);
    }
}
