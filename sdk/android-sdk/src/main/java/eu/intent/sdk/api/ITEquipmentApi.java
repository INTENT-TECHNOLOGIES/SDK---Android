package eu.intent.sdk.api;

import android.content.Context;

import java.util.List;
import java.util.Locale;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITAssetType;
import eu.intent.sdk.model.ITEquipment;
import eu.intent.sdk.model.ITEquipmentList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A wrapper to call the Equipment API.
 */
public class ITEquipmentApi {
    private Service mService;

    public ITEquipmentApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Retrieves the equipments of the given part.
     *
     * @param partId the ITPart's ID
     */
    public void getByPartId(String partId, ITApiCallback<ITEquipmentList> callback) {
        mService.getByAssetId(ITAssetType.PART.toString(), partId, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given part and matching one of the given types.
     *
     * @param partId the ITPart's ID
     * @param types  an array of Types to filter the result
     */
    public void getByPartId(String partId, List<ITEquipment.Type> types, ITApiCallback<ITEquipmentList> callback) {
        String[] typeNames = new String[types.size()];
        for (int i = 0; i < types.size(); i++) {
            typeNames[i] = types.get(i).name();
        }
        mService.getByAssetId(ITAssetType.PART.toString(), partId, typeNames, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given part.
     *
     * @param partRef the ITPart's external ref
     */
    public void getByPartRef(String partRef, ITApiCallback<ITEquipmentList> callback) {
        mService.getByAssetRef(ITAssetType.PART.toString(), partRef, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given part and matching one of the given types.
     *
     * @param partRef the ITPart's external ref
     * @param types   an array of Types to filter the result
     */
    public void getByPartRef(String partRef, List<ITEquipment.Type> types, ITApiCallback<ITEquipmentList> callback) {
        String[] typeNames = new String[types.size()];
        for (int i = 0; i < types.size(); i++) {
            typeNames[i] = types.get(i).name();
        }
        mService.getByAssetRef(ITAssetType.PART.toString(), partRef, typeNames, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given site.
     *
     * @param siteId the ITSite's ID
     */
    public void getBySiteId(String siteId, ITApiCallback<ITEquipmentList> callback) {
        mService.getByAssetId(ITAssetType.SITE.toString(), siteId, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given site and matching one of the given types.
     *
     * @param siteId the ITSite's ID
     * @param types  an array of Types to filter the result
     */
    public void getBySiteId(String siteId, List<ITEquipment.Type> types, ITApiCallback<ITEquipmentList> callback) {
        String[] typeNames = new String[types.size()];
        for (int i = 0; i < types.size(); i++) {
            typeNames[i] = types.get(i).name();
        }
        mService.getByAssetId(ITAssetType.SITE.toString(), siteId, typeNames, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given site.
     *
     * @param siteRef the ITSite's external ref
     */
    public void getBySiteRef(String siteRef, ITApiCallback<ITEquipmentList> callback) {
        mService.getByAssetRef(ITAssetType.SITE.toString(), siteRef, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the given site and matching one of the given types.
     *
     * @param siteRef the ITSite's external ref
     * @param types   an array of Types to filter the result
     */
    public void getBySiteRef(String siteRef, List<ITEquipment.Type> types, ITApiCallback<ITEquipmentList> callback) {
        String[] typeNames = new String[types.size()];
        for (int i = 0; i < types.size(); i++) {
            typeNames[i] = types.get(i).name();
        }
        mService.getByAssetRef(ITAssetType.SITE.toString(), siteRef, typeNames, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipment with the given ID.
     *
     * @param equipmentId the equipment ID
     */
    public void getById(String equipmentId, ITApiCallback<ITEquipment> callback) {
        mService.get(equipmentId, true, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipment with the given reference.
     *
     * @param equipmentRef the equipment reference
     */
    public void getByRef(String equipmentRef, ITApiCallback<ITEquipment> callback) {
        mService.get(equipmentRef, false, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the equipments of the user's domain, matching the given query.
     *
     * @param query a query string to filter the results
     */
    public void search(String query, ITApiCallback<ITEquipmentList> callback) {
        mService.get(query, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("datahub/v1/equips")
        Call<ITEquipmentList> getByAssetId(@Query("assetType") String assetType, @Query("assetId") String assetId, @Query("type") String[] types, @Query("page") int page, @Query("countByPage") int count, @Query("lang") String lang);

        @GET("datahub/v1/equips")
        Call<ITEquipmentList> getByAssetRef(@Query("assetType") String assetType, @Query("assetRef") String assetRef, @Query("type") String[] types, @Query("page") int page, @Query("countByPage") int count, @Query("lang") String lang);

        @GET("datahub/v1/equips/{externalRef}")
        Call<ITEquipment> get(@Path("externalRef") String partRefOrId, @Query("byId") boolean useId, @Query("lang") String lang);

        @GET("datahub/v1/equips")
        Call<ITEquipmentList> get(@Query("query") String query, @Query("page") int page, @Query("countByPage") int count, @Query("lang") String lang);
    }
}
