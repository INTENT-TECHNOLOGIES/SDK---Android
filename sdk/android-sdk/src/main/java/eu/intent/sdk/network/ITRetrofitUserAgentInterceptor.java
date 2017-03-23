package eu.intent.sdk.network;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * An interceptor to add the User-Agent header needed by the API.
 */
public class ITRetrofitUserAgentInterceptor implements Interceptor {
    private static final String HEADER = "User-Agent";
    private static final String ENCODING = "ISO-8859-1";

    private String mUserAgent;

    public ITRetrofitUserAgentInterceptor(@NonNull Context context) {
        try {
            mUserAgent = getUserAgent(context);
        } catch (PackageManager.NameNotFoundException | UnsupportedEncodingException e) {
            mUserAgent = "";
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!TextUtils.isEmpty(mUserAgent)) {
            request = request.newBuilder()
                    .header(HEADER, mUserAgent)
                    .build();
        }
        return chain.proceed(request);
    }

    @NonNull
    private String getUserAgent(@NonNull Context context) throws PackageManager.NameNotFoundException, UnsupportedEncodingException {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        Resources res = context.getResources();
        String appName = res.getString(packageInfo.applicationInfo.labelRes);
        String appVersion = packageInfo.versionName;
        String userAgent = TextUtils.join(" ", new String[]{appName, appVersion}).trim();
        return URLEncoder.encode(userAgent, ENCODING);
    }
}
