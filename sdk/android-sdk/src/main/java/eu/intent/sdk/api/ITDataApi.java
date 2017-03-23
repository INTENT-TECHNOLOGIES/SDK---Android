package eu.intent.sdk.api;

import com.google.gson.annotations.SerializedName;

import eu.intent.sdk.model.ITData;
import eu.intent.sdk.model.ITDataSet;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITDataApi {
    @GET("datahub/v1/equips/{externalRef}/activities/{activityKey}/{dataType}")
    Call<ITDataSet> getByEquipment(@Path("externalRef") String equipmentRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @Query("byId") boolean byId, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("since") String since, @Query("page") int page, @Query("countByPage") int countByPage);

    @GET("datahub/v1/parts/{externalRef}/activities/{activityKey}/{dataType}")
    Call<ITDataSet> getByPart(@Path("externalRef") String partRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @Query("byId") boolean byId, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("since") String since, @Query("page") int page, @Query("countByPage") int countByPage);

    @GET("datahub/v1/sites/{externalRef}/activities/{activityKey}/{dataType}")
    Call<ITDataSet> getBySite(@Path("externalRef") String siteRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @Query("byId") boolean byId, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("since") String since, @Query("page") int page, @Query("countByPage") int countByPage);

    @GET("datahub/v1/streams/{streamId}/{dataType}")
    Call<ITDataSet> getByStream(@Path("streamId") String streamId, @Path("dataType") String dataType, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("since") String since, @Query("page") int page, @Query("countByPage") int countByPage);

    @POST("datahub/v1/equips/{externalRef}/activities/{activityKey}/{dataType}")
    Call<Void> publishToEquipment(@Path("externalRef") String equipmentRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @Query("byId") boolean byId, @Body NewData body);

    @POST("datahub/v1/parts/{externalRef}/activities/{activityKey}/{dataType}")
    Call<Void> publishToPart(@Path("externalRef") String partRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @Query("byId") boolean byId, @Body NewData body);

    @POST("datahub/v1/sites/{externalRef}/activities/{activityKey}/{dataType}")
    Call<Void> publishToSite(@Path("externalRef") String siteRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @Query("byId") boolean byId, @Body NewData body);

    @POST("datahub/v1/streams/{streamId}/{dataType}")
    Call<Void> publishToStream(@Path("streamId") String streamId, @Path("dataType") String dataType, @Query("byReference") boolean byReference, @Body NewData body);

    final class NewData {
        public long timestamp;
        public double value;
        @SerializedName("trustlevel")
        public ITData.TrustLevel trustLevel;

        public NewData(long timestamp, double value, ITData.TrustLevel trustLevel) {
            this.timestamp = timestamp;
            this.value = value;
            this.trustLevel = trustLevel;
        }
    }
}