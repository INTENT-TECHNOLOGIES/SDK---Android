package eu.intent.sdk.api;

import java.util.List;

import eu.intent.sdk.model.ITActivityKeys;
import eu.intent.sdk.model.ITSite;
import eu.intent.sdk.model.ITSiteList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITSiteApi {
    @GET("datahub/v1/sites")
    Call<ITSiteList> get(@Query("query") String query, @Query("updatedSince") String updatedSince, @Query("page") int page, @Query("countByPage") int countByPage, @Query("lang") String lang);

    @GET("datahub/v1/sites/{externalRef}")
    Call<ITSite> get(@Path("externalRef") String siteIdOrRef, @Query("lang") String lang, @Query("byId") boolean byId);

    @GET("datahub/v1/sites/{externalRef}/activities")
    Call<ITActivityKeys> getActivities(@Path("externalRef") String siteIdOrRef, @Query("byId") boolean byId);

    @GET("datahub/v1/sites/near?onlyIds=false")
    Call<List<ITSite>> getAroundLocation(@Query("lat") double lat, @Query("lng") double lng, @Query("maxDistance") int maxDistance);

    @GET("datahub/v1/sites/near?onlyIds=true")
    Call<List<String>> getIdsAroundLocation(@Query("lat") double lat, @Query("lng") double lng, @Query("maxDistance") int maxDistance);
}