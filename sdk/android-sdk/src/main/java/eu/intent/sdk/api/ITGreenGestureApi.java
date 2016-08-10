package eu.intent.sdk.api;

import android.content.Context;
import android.text.TextUtils;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITEnergy;
import eu.intent.sdk.model.ITGreenGestureList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * A wrapper to call the Green Gesture API.
 */
public class ITGreenGestureApi {
    private Service mService;

    public ITGreenGestureApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Gets the green gestures proposed to the authenticated user.
     */
    public void get(ITApiCallback<ITGreenGestureList> callback) {
        get(null, callback);
    }

    /**
     * Gets the green gestures proposed to the authenticated user.
     *
     * @param energies an array of ITEnergy
     */
    public void get(ITEnergy[] energies, ITApiCallback<ITGreenGestureList> callback) {
        String typesString = TextUtils.join(",", energies != null && energies.length > 0 ? energies : ITEnergy.values());
        mService.get(typesString, 1, Integer.MAX_VALUE).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("v1/green-gestures")
        Call<ITGreenGestureList> get(@Query("energy") String energies, @Query("page") int page, @Query("countByPage") int count);
    }
}
