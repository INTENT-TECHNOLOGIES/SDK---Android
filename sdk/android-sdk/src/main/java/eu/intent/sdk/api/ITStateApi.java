package eu.intent.sdk.api;

import java.util.List;

import eu.intent.sdk.model.ITState;
import eu.intent.sdk.model.ITStateTemplate;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ITStateApi {
    @GET("v1/states/current/domain")
    Call<List<ITState>> getCurrent(@Query("status") String status);

    @GET("v1/states/current/position")
    Call<List<ITState>> getCurrentAroundLocation(@Query("lat") double lat, @Query("lng") double lng, @Query("distance") double distance, @Query("status") String status, @Query("templateId") String templateId);

    @GET("v1/states/current/assets")
    Call<List<ITState>> getCurrentByAssets(@Query("assetType") String assetType, @Query("assetIds") String[] assetIds, @Query("status") String status, @Query("templateId") String templateId);

    @GET("v1/states/current/streams")
    Call<List<ITState>> getCurrentByStreams(@Query("streamIds") String[] assetIds, @Query("status") String status, @Query("templateId") String templateId);

    @GET("v1/states/history/position")
    Call<List<ITState>> getHistoryAroundLocation(@Query("lat") double lat, @Query("lng") double lng, @Query("distance") double distance, @Query("fromTimestamp") long from, @Query("toTimestamp") long to, @Query("status") String status, @Query("templateId") String templateId);

    @GET("v1/states/history/assets")
    Call<List<ITState>> getHistoryByAssets(@Query("assetType") String assetType, @Query("assetIds") String[] assetIds, @Query("fromTimestamp") long from, @Query("toTimestamp") long to, @Query("status") String status, @Query("templateId") String templateId);

    @GET("v1/states/history/streams")
    Call<List<ITState>> getHistoryByStreams(@Query("streamIds") String[] assetIds, @Query("fromTimestamp") long from, @Query("toTimestamp") long to, @Query("status") String status, @Query("templateId") String templateId);

    @GET("v1/states/templates")
    Call<List<ITStateTemplate>> getTemplates(@Query("summary") boolean summary);
}