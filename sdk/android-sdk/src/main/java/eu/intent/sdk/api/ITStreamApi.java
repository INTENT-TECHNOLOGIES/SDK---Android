package eu.intent.sdk.api;

import java.util.List;

import eu.intent.sdk.model.ITStream;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITStreamApi {
    @GET("datahub/v1/streams/{streamId}")
    Call<ITStream> get(@Path("streamId") String streamId);

    @GET("datahub/v1/equips/{externalRef}/streams")
    Call<List<String>> getByEquipment(@Path("externalRef") String equipmentIdOrRef, @Query("byId") boolean byId, @Query("activityKey") String... activities);

    @GET("datahub/v1/parts/{externalRef}/streams")
    Call<List<String>> getByPart(@Path("externalRef") String partIdOrRef, @Query("byId") boolean byId, @Query("activityKey") String... activities);

    @GET("datahub/v1/sites/{externalRef}/streams")
    Call<List<String>> getBySite(@Path("externalRef") String siteIdOrRef, @Query("withPartStreams") boolean withPartStreams, @Query("byId") boolean byId, @Query("activityKey") String... activities);
}