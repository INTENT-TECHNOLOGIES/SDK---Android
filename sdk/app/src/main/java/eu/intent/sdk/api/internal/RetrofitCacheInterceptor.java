package eu.intent.sdk.api.internal;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import eu.intent.sdk.util.ITConnectivityHelper;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An interceptor to manage the cache when calling the API.
 */
public class RetrofitCacheInterceptor implements Interceptor {
    private Context mContext;

    public RetrofitCacheInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        if (ITConnectivityHelper.isNetworkAvailable(mContext)) {
            // Force network, don't use cache
            requestBuilder.cacheControl(CacheControl.FORCE_NETWORK).build();
        } else if (request.method().equals("GET")) {
            // Use cache
            Log.d(getClass().getName(), "No network: force cache");
            requestBuilder.cacheControl(CacheControl.FORCE_CACHE).build();
        }
        request = requestBuilder.build();
        return chain.proceed(request);
    }
}
