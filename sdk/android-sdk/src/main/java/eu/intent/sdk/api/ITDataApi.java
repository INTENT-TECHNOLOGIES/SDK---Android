package eu.intent.sdk.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    public static final String SORT_PROPERTY_TIMESTAMP = "timestamp";

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
     * @param sort         the sort property and order
     * @param countByPage  the number of results by page
     * @param page         the page you want to load (page index starts at 1)
     */
    public void getByPartRef(@NonNull String partRef, @NonNull ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, ITSort sort, int countByPage, int page, ITApiCallback<List<ITDataResult>> callback) {
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        String sortString = getSortString(sort);
        mService.getByPart(partRef, dataType.toString(), activityKeys, "0", null, page, countByPage, false, byDay, frequency, sortString).enqueue(new ProxyCallback<>(callback));
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
    public void getByPartRef(@NonNull String partRef, @NonNull ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        mService.getByPart(partRef, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, false, byDay, frequency, null).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data for a part and an array of activities. The result will be a list of ITDataResults, grouped by activity.
     *
     * @param partId       the ITPart's Intent id
     * @param dataType     an ITDataType depending on what you want to retrieve
     * @param activityKeys the keys of the concerned activities
     * @param granularity  the data granularity
     * @param sort         the sort property and order
     * @param countByPage  the number of results by page
     * @param page         the page you want to load (page index starts at 1)
     */
    public void getByPartId(@NonNull String partId, @NonNull ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, ITSort sort, int countByPage, int page, ITApiCallback<List<ITDataResult>> callback) {
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        String sortString = getSortString(sort);
        mService.getByPart(partId, dataType.toString(), activityKeys, "0", null, page, countByPage, true, byDay, frequency, sortString).enqueue(new ProxyCallback<>(callback));
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
    public void getByPartId(@NonNull String partId, @NonNull ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        mService.getByPart(partId, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, true, byDay, frequency, null).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data for a site and an array of activities. The result will be a list of ITDataResults, grouped by activity.
     *
     * @param siteRef      the ITSite's external ref
     * @param dataType     an ITDataType depending on what you want to retrieve
     * @param activityKeys the keys of the concerned activities
     * @param granularity  the data granularity
     * @param sort         the sort property and order
     * @param countByPage  the number of results by page
     * @param page         the page you want to load (page index starts at 1)
     */
    public void getBySiteRef(@NonNull String siteRef, @NonNull ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, ITSort sort, int countByPage, int page, ITApiCallback<List<ITDataResult>> callback) {
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        String sortString = getSortString(sort);
        mService.getBySite(siteRef, dataType.toString(), activityKeys, "0", null, page, countByPage, false, byDay, frequency, sortString).enqueue(new ProxyCallback<>(callback));
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
    public void getBySiteRef(@NonNull String siteRef, @NonNull ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        mService.getBySite(siteRef, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, false, byDay, frequency, null).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data for a site and an array of activities. The result will be a list of ITDataResults, grouped by activity.
     *
     * @param siteId       the ITSite's Intent id
     * @param dataType     an ITDataType depending on what you want to retrieve
     * @param activityKeys the key of the concerned activities
     * @param granularity  the data granularity
     * @param sort         the sort property and order
     * @param countByPage  the number of results by page
     * @param page         the page you want to load (page index starts at 1)
     */
    public void getBySiteId(@NonNull String siteId, @NonNull ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, ITSort sort, int countByPage, int page, ITApiCallback<List<ITDataResult>> callback) {
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        String sortString = getSortString(sort);
        mService.getBySite(siteId, dataType.toString(), activityKeys, "0", null, page, countByPage, true, byDay, frequency, sortString).enqueue(new ProxyCallback<>(callback));
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
    public void getBySiteId(@NonNull String siteId, @NonNull ITData.Type dataType, String[] activityKeys, ITData.Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        Integer frequency = ITData.Granularity.MANUAL.equals(granularity) ? 0 : null;
        mService.getBySite(siteId, dataType.toString(), activityKeys, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, true, byDay, frequency, null).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the data from one or several streams. The result will be a list of ITDataResults, grouped by stream.
     *
     * @param dataType    an ITDataType depending on what you want to retrieve
     * @param streamIds   the ITStreams' IDs
     * @param granularity the data granularity
     * @param sort        the sort property and order
     * @param countByPage the number of results by page
     * @param page        the page you want to load (page index starts at 1)
     */
    public void getByStream(@NonNull ITData.Type dataType, String[] streamIds, ITData.Granularity granularity, ITSort sort, int countByPage, int page, ITApiCallback<List<ITDataResult>> callback) {
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        String sortString = getSortString(sort);
        mService.getByStream(dataType.toString(), streamIds, "0", null, page, countByPage, byDay, sortString).enqueue(new ProxyCallback<>(callback));
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
    public void getByStream(@NonNull ITData.Type dataType, String[] streamIds, ITData.Granularity granularity, long startTime, long endTime, int page, ITApiCallback<List<ITDataResult>> callback) {
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        boolean byDay = ITData.Granularity.DAY.equals(granularity);
        mService.getByStream(dataType.toString(), streamIds, startTimeIso8601, endTimeIso8601, page, Service.MAX_COUNT, byDay, null).enqueue(new ProxyCallback<>(callback));
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
    public void publishToPartWithId(@NonNull String partId, @NonNull String activityKey, ITData.Type dataType, long timestamp, double value, ITData.TrustLevel trustLevel, ITApiCallback<Void> callback) {
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
    public void publishToPartWithRef(@NonNull String partRef, @NonNull String activityKey, @NonNull ITData.Type dataType, long timestamp, double value, ITData.TrustLevel trustLevel, ITApiCallback<Void> callback) {
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
    public void publishToSite(@NonNull String siteRef, @NonNull String activityKey, @NonNull ITData.Type dataType, long timestamp, double value, ITData.TrustLevel trustLevel, ITApiCallback<Void> callback) {
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
    public void publishToStream(@NonNull String streamId, @NonNull ITData.Type dataType, long timestamp, double value, ITData.TrustLevel trustLevel, ITApiCallback<Void> callback) {
        mService.publishToStream(streamId, dataType.toString(), new Service.Body(timestamp, value, trustLevel)).enqueue(new ProxyCallback<>(callback));
    }

    @Nullable
    private String getSortString(@Nullable ITSort sort) {
        String sortString = null;
        if (sort != null) {
            sortString = sort.toString();
        }
        return sortString;
    }

    private interface Service {
        int MAX_COUNT = 31 * 24;

        @GET("datahub/v1/parts/{externalRef}/{dataType}")
        Call<List<ITDataResult>> getByPart(@Path("externalRef") String partRef, @Path("dataType") String dataType, @Query("activityKey") String[] activityKeys, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("page") int page, @Query("countByPage") int count, @Query("byId") boolean byId, @Query("dataByDay") boolean dataByDay, @Query("frequency") Integer frequency, @Query("sort") String sort);

        @GET("datahub/v1/sites/{externalRef}/{dataType}")
        Call<List<ITDataResult>> getBySite(@Path("externalRef") String siteRef, @Path("dataType") String dataType, @Query("activityKey") String[] activityKeys, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("page") int page, @Query("countByPage") int count, @Query("byId") boolean byId, @Query("dataByDay") boolean dataByDay, @Query("frequency") Integer frequency, @Query("sort") String sort);

        @GET("datahub/v1/streams/{dataType}")
        Call<List<ITDataResult>> getByStream(@Path("dataType") String dataType, @Query("streamId") String[] streamIds, @Query("startTime") String startTime, @Query("endTime") String endTime, @Query("page") int page, @Query("countByPage") int count, @Query("dataByDay") boolean dataByDay, @Query("sort") String sort);

        @POST("datahub/v1/parts/{externalRef}/activities/{activityKey}/{dataType}")
        Call<Void> publishToPart(@Path("externalRef") String partRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @Query("byId") boolean usePartId, @retrofit2.http.Body Body body);

        @POST("datahub/v1/sites/{externalRef}/activities/{activityKey}/{dataType}")
        Call<Void> publishToSite(@Path("externalRef") String siteRef, @Path("activityKey") String activityKey, @Path("dataType") String dataType, @retrofit2.http.Body Body body);

        @POST("datahub/v1/streams/{streamId}/{dataType}")
        Call<Void> publishToStream(@Path("streamId") String streamId, @Path("dataType") String dataType, @retrofit2.http.Body Body body);

        final class Body {
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
