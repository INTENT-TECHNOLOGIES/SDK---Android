package eu.intent.sdk.model;

import android.content.Context;

import java.util.List;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * A device is deployed on a site, part or equipment, and sends data to the platform.
 *
 * @see ITDeviceType
 */
public final class ITDevice {
    private static Service sService;

    private ITDevice() {
    }

    /**
     * Retrieves all the device types handled by the platform.
     */
    public static void getTypes(Context context, ITApiCallback<List<ITDeviceType>> callback) {
        getServiceInstance(context).get().enqueue(new ProxyCallback<>(callback));
    }

    private static Service getServiceInstance(Context context) {
        if (sService == null) {
            sService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
        }
        return sService;
    }

    private interface Service {
        @GET("dm/v1/deviceTypes")
        Call<List<ITDeviceType>> get();
    }
}
