package eu.intent.sdk.api;

import android.content.Context;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITTagList;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * A wrapper to call the Tag API.
 */
public class ITTagApi {
    private Service mService;

    public ITTagApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Loads all the existing tags for the authenticated user.
     */
    public void loadAll(ITApiCallback<ITTagList> callback) {
        mService.getAll().enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("datahub/v1/users/system/classifications/tags")
        Call<ITTagList> getAll();
    }
}
