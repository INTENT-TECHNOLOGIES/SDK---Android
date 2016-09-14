package eu.intent.sdk.api;

import android.content.Context;

import java.util.List;
import java.util.Locale;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITActivity;
import eu.intent.sdk.model.ITActivityCategory;
import eu.intent.sdk.model.ITActivityKeys;
import eu.intent.sdk.model.ITActivityList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A wrapper to call the Activity API.
 */
public class ITActivityApi {
    private Service mService;

    public ITActivityApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Retrieves the activities list.
     */
    public void get(ITApiCallback<ITActivityList> callback) {
        mService.get(Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities grouped by category. The result will be a list of ITActivityCategory, containing the activities IDs.
     */
    public void getGroupedByCategory(ITApiCallback<List<ITActivityCategory>> callback) {
        mService.getByCategory().enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activity with the given ID.
     *
     * @param id the activity ID
     */
    public void getById(String id, ITApiCallback<ITActivity> callback) {
        mService.get(id, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities of a given equipment. The callback returns the activity keys.
     *
     * @param equipmentRef the ITEquipment's external ref
     */
    public void getByEquipmentRef(String equipmentRef, ITApiCallback<ITActivityKeys> callback) {
        mService.getByEquipment(equipmentRef, false).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities of a given equipment by Intent Id. The callback returns the activity keys.
     *
     * @param equipmentId the ITEquipment's Intent id
     */
    public void getByEquipmentId(String equipmentId, ITApiCallback<ITActivityKeys> callback) {
        mService.getByEquipment(equipmentId, true).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities of a given part. The callback returns the activity keys.
     *
     * @param partRef the ITPart's external ref
     */
    public void getByPartRef(String partRef, ITApiCallback<ITActivityKeys> callback) {
        mService.getByPart(partRef, false).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities of a given part by Intent Id. The callback returns the activity keys.
     *
     * @param partId the ITPart's Intent id
     */
    public void getByPartId(String partId, ITApiCallback<ITActivityKeys> callback) {
        mService.getByPart(partId, true).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities of a given site. The callback returns the activity keys.
     *
     * @param siteRef the ITSite's external ref
     */
    public void getBySiteRef(String siteRef, ITApiCallback<ITActivityKeys> callback) {
        mService.getBySite(siteRef, false).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities of a given site by Intent Id. The callback returns the activity keys.
     *
     * @param siteId the ITSite's Intent id
     */
    public void getBySiteId(String siteId, ITApiCallback<ITActivityKeys> callback) {
        mService.getBySite(siteId, true).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("datahub/v1/activities")
        Call<ITActivityList> get(@Query("lang") String lang);

        @GET("datahub/v1/activities/{activityKey}")
        Call<ITActivity> get(@Path("activityKey") String id, @Query("lang") String lang);

        @GET("datahub/v1/equips/{externalRef}/activities")
        Call<ITActivityKeys> getByEquipment(@Path("externalRef") String equipRef, @Query("byId") boolean byId);

        @GET("datahub/v1/parts/{externalRef}/activities")
        Call<ITActivityKeys> getByPart(@Path("externalRef") String partRef, @Query("byId") boolean byId);

        @GET("datahub/v1/sites/{externalRef}/activities")
        Call<ITActivityKeys> getBySite(@Path("externalRef") String siteRef, @Query("byId") boolean byId);

        @GET("datahub/v1/themes")
        Call<List<ITActivityCategory>> getByCategory();
    }
}
