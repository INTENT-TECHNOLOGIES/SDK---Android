package eu.intent.sdk.api.internal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * An interceptor to manage the cache when calling the API.
 */
public class RetrofitCacheInterceptor implements Interceptor {
    private ConnectivityManager mConnectivityManager;

    public RetrofitCacheInterceptor(@NonNull Context context) {
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
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
