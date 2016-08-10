package eu.intent.sdk.api;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITData;
import eu.intent.sdk.model.ITDataResult;
import eu.intent.sdk.util.ITDateUtils;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A wrapper to call the Data API.
 */
public class ITDataApi {
    private Service mService;

    public ITDataApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Retrieves the data for a part and an array of activities. The result will be a list of ITDataResults, grouped by activity.
     *
     * @param partRef      the ITPart's external ref
     * @param dataType     an ITDataType depending on what you want to retrieve
     * @param activityKeys the keys of the concerned activities
     * @param granularity  the data granularity
     * @param startTime    the oldest date limit you're interested in
     * @param endTime      the newest date limit you're interested in
     * @param page         the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public void getByPartRef(String partRef, ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        mService.getByPart(partRef, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, false, byDay, frequency).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data for a part and an array of activities. The result will be a list of ITDataResults, grouped by activity.
     *
     * @param partId       the ITPart's Intent id
     * @param dataType     an ITDataType depending on what you want to retrieve
     * @param activityKeys the keys of the concerned activities
     * @param granularity  the data granularity
     * @param startTime    the oldest date limit you're interested in
     * @param endTime      the newest date limit you're interested in
     * @param page         the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public void getByPartId(String partId, ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        mService.getByPart(partId, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, true, byDay, frequency).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data for a site and an array of activities. The result will be a list of ITDataResults, grouped by activity.
     *
     * @param siteRef      the ITSite's external ref
     * @param dataType     an ITDataType depending on what you want to retrieve
     * @param activityKeys the keys of the concerned activities
     * @param startTime    the oldest date limit you're interested in
     * @param endTime      the newest date limit you're interested in
     * @param granularity  the data granularity
     * @param page         the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public void getBySiteRef(String siteRef, ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        mService.getBySite(siteRef, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, false, byDay, frequency).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data for a site and an array of activities. The result will be a list of ITDataResults, grouped by activity.
     *
     * @param siteId       the ITSite's Intent id
     * @param dataType     an ITDataType depending on what you want to retrieve
     * @param activityKeys the key of the concerned activities
     * @param granularity  the data granularity
     * @param startTime    the oldest date limit you're interested in
     * @param endTime      the newest date limit you're interested in
     * @param page         the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public void getBySiteId(String siteId, ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        mService.getBySite(siteId, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, true, byDay, frequency).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data from one or several streams. The result will be a list of ITDataResults, grouped by stream.
     *
     * @param dataType    an ITDataType depending on what you want to retrieve
     * @param streamIds   the ITStreams' IDs
     * @param granularity the data granularity
     * @param startTime   the oldest date limit you're interested in
     * @param endTime     the newest date limit you're interested in
     * @param page        the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public void getByStream(ITData.Type dataType, String[] streamIds, ITData.Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        mService.getByStream(dataType.toString(), streamIds, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, ITData.Granularity.DAY.equals(granularity)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Publishes a piece of data to a part.
     *
     * @param partId      the ITPart's ID
     * @param activityKey the key of the concerned activity
     * @param dataType    an ITDataType depending on what you want to publish
     * @param timestamp   the data timestamp
     * @param value       the data value
     * @param trustLevel  a trust level depending on where (or who) you data comes from
     */
    public void publishToPartWithId(String partId, String activityKey, ITData.Type dataType, long timestamp, double value, ITData.TrustLevel trustLevel, ITApiCallback<Void> callback) {
        mService.publishToPart(partId, activityKey, dataType.toString(), true, new Service.Body(timestamp, value, trustLevel)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Publishes a piece of data to a part.
     *
     * @param partRef     the ITPart's external ref
     * @param activityKey the key of the concerned activity
     * @param dataType    an ITDataType depending on what you want to publish
     * @param timestamp   the data timestamp
     * @param value       the data value
     * @param trustLevel  a trust level depending on where (or who) you data comes from
     */
    public void publishToPartWithRef(String partRef, String activityKey, ITData.Type dataType, long timestamp, double value, ITData.TrustLevel trustLevel, ITApiCallback<Void> callback) {
        mService.publishToPart(partRef, activityKey, dataType.toString(), false, new Service.Body(timestamp, value, trustLevel)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Publishes a piece of data to a site.
     *
     * @param siteRef     the ITSite's external ref
     * @param activityKey the key of the concerned activity
     * @param dataType    an ITDataType depending on what you want to publish
     * @param timestamp   the data timestamp
     * @param value       the data value
     * @param trustLevel  a trust level depending on where (or who) you data comes from
     */
    public void publishToSite(String siteRef, String activityKey, ITData.Type dataType, long timestamp, double value, ITData.TrustLevel trustLevel, ITApiCallback<Void> callback) {
        mService.publishToSite(siteRef, activityKey, dataType.toString(), new Service.Body(timestamp, value, trustLevel)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Publishes a piece of data to a stream.
     *
     * @param streamId   the ITStream's ID
     * @param dataType   an ITDataType depending on what you want to publish
     * @param timestamp  the data timestamp
     * @param value      the data value
     * @param trustLevel a trust level depending on where (or who) you data comes from
     */
    public void publishToStream(String streamId, ITData.Type dataType, long timestamp, double value, ITData.TrustLevel trustLevel, ITApiCallback<Void> callback) {
        mService.publishToStream(streamId, dataType.toString(), new Service.Body(timestamp, value, trustLevel)).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        int MAX_COUNT = 31 * 24;

        @GET("datahub/v1/parts/{externalRef}/{dataType}")
        Call<List<ITDataResult>> getByPart(@Path("externalRef") String partRef, @Path("dataType") String dataType, @Query("activityKey") String[] activityKeys, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("page") int page, @Query("countByPage") int count, @Query("byId") boolean byId, @Query("dataByDay") boolean dataByDay, @Query("frequency") Integer frequency);

        @GET("datahub/v1/sites/{externalRef}/{dataType}")
        Call<List<ITDataResult>> getBySite(@Path("externalRef") String siteRef, @Path("dataType") String dataType, @Query("activityKey") String[] activityKeys, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("page") int page, @Query("countByPage") int count, @Query("byId") boolean byId, @Query("dataByDay") boolean dataByDay, @Query("frequency") Integer frequency);

        @GET("datahub/v1/streams/{dataType}")
        Call<List<ITDataResult>> getByStream(@Path("dataType") String dataType, @Query("streamId") String[] streamIds, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("page") int page, @Query("countByPage") int count, @Query("dataByDay") boolean dataByDay);

        @POST("datahub/v1/parts/{externalRef}/activities/{activityKey}/{dataType}")
        Call<Void> publishToPart(@Path("externalRef") String partRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @Query("byId") boolean usePartId, @retrofit2.http.Body Body body);

        @POST("datahub/v1/sites/{externalRef}/activities/{activityKey}/{dataType}")
        Call<Void> publishToSite(@Path("externalRef") String siteRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @retrofit2.http.Body Body body);

        @POST("datahub/v1/streams/{streamId}/{dataType}")
        Call<Void> publishToStream(@Path("streamId") String streamId, @Path("dataType") String dataType, @retrofit2.http.Body Body body);

        class Body {
            public long timestamp;
            public double value;
            @SerializedName("trustlevel")
            public ITData.TrustLevel trustLevel;

            private Body(long timestamp, double value, ITData.TrustLevel trustLevel) {
                this.timestamp = timestamp;
                this.value = value;
                this.trustLevel = trustLevel;
            }
        }
    }
}
