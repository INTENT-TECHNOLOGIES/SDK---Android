package eu.intent.sdk.api;

import java.util.List;

import eu.intent.sdk.model.ITActivityKeys;
import eu.intent.sdk.model.ITEquipment;
import eu.intent.sdk.model.ITEquipmentList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITEquipmentApi {
    @GET("datahub/v1/equips/{externalRef}")
    Call<ITEquipment> get(@Path("externalRef") String ref, @Query("lang") String lang, @Query("byId") boolean byId);

    @GET("datahub/v1/equips/{externalRef}/activities")
    Call<ITActivityKeys> getActivities(@Path("externalRef") String ref, @Query("byId") boolean byId);

    @GET("datahub/v1/equips")
    Call<ITEquipmentList> getByAsset(@Query("query") String query, @Query("assetType") String assetType, @Query("assetRef") String assetRef, @Query("type") String[] types, @Query("updatedSince") String updatedSince, @Query("fromRemovalDate") String fromRemovalDate, @Query("endRemovalDate") String endRemovalDate, @Query("page") int page, @Query("countByPage") int countByPage, @Query("lang") String lang);

    @GET("datahub/v1/equips/nearBy?onlyIds=true")
    Call<List<String>> getIdsNearby(@Query("lat") double lat, @Query("lng") double lng, @Query("maxDistance") int maxDistance);

    @GET("datahub/v1/equips/nearBy")
    Call<List<ITEquipment>> getNearby(@Query("lat") double lat, @Query("lng") double lng, @Query("maxDistance") int maxDistance);
}