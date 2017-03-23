package eu.intent.sdk.api;

import eu.intent.sdk.model.ITActivityKeys;
import eu.intent.sdk.model.ITPart;
import eu.intent.sdk.model.ITPartList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITPartApi {
    @GET("datahub/v1/parts/{externalRef}")
    Call<ITPart> get(@Path("externalRef") String partRefOrId, @Query("lang") String lang, @Query("withUsers") boolean withUsers, @Query("byId") boolean byId);

    @GET("datahub/v1/parts/{externalRef}/activities")
    Call<ITActivityKeys> getActivities(@Path("externalRef") String partRefOrId, @Query("byId") boolean byId);

    @GET("datahub/v1/parts")
    Call<ITPartList> getBySiteRef(@Query("siteExternalRef") String siteRef, @Query("query") String query, @Query("updatedSince") String updatedSince, @Query("page") int page, @Query("countByPage") int countByPage, @Query("withUsers") boolean withUsers, @Query("lang") String lang);

    @GET("datahub/v1/parts")
    Call<ITPartList> getBySiteId(@Query("siteId") String siteId, @Query("query") String query, @Query("updatedSince") String updatedSince, @Query("page") int page, @Query("countByPage") int countByPage, @Query("withUsers") boolean withUsers, @Query("lang") String lang);

    @GET("datahub/v1/parts/mine")
    Call<ITPart> getMine();
}