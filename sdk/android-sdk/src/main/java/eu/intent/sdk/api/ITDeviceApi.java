package eu.intent.sdk.api;

import android.content.Context;

import java.util.List;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITDeviceType;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * A wrapper to call the Device API.
 */
public class ITDeviceApi {
    private Service mService;

    public ITDeviceApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Retrieves all the device types handled by the platform.
     */
    public void getTypes(ITApiCallback<List<ITDeviceType>> callback) {
        mService.getDeviceTypes().enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("dm/v1/deviceTypes")
        Call<List<ITDeviceType>> getDeviceTypes();
    }
}
