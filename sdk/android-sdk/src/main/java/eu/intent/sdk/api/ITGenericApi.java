package eu.intent.sdk.api;

import eu.intent.sdk.api.internal.ProxyCallback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * A wrapper to call any API.
 */

public class ITGenericApi {
    private Service mService;

    public ITGenericApi() {
        // The service URL will override the base URL
        mService = new Retrofit.Builder().baseUrl("http://whatever").build().create(Service.class);
    }

    /**
     * Gets the response of the given URL as String.
     */
    public void get(String url, ITApiCallback<ResponseBody> callback) {
        mService.get(url).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET
        Call<ResponseBody> get(@Url String url);
    }
}
