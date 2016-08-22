package eu.intent.sdk.api;

import android.content.Context;

import java.util.List;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITState;
import eu.intent.sdk.model.ITStateTemplate;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * A wrapper to call the State API.
 */
public class ITStateApi {
    private Service mService;

    public ITStateApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Retrieves the current states of the user's domain.
     */
    public void getCurrent(ITApiCallback<List<ITState>> callback) {
        getCurrent(null, callback);
    }

    /**
     * Retrieves the current states of the user's domain.
     *
     * @param status the status of the states you want to retrieve if you want to filter by status
     */
    public void getCurrent(String status, ITApiCallback<List<ITState>> callback) {
        mService.getCurrent(status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the current states of the parts with the given refs.
     *
     * @param partRefs the ITParts' external refs
     */
    public void getCurrentByParts(String[] partRefs, ITApiCallback<List<ITState>> callback) {
        getCurrentByParts(partRefs, null, callback);
    }

    /**
     * Retrieves the current states around the given location.
     *
     * @param lat    the location latitude
     * @param lng    the location longitude
     * @param radius the radius in meters around the location
     */
    public void getCurrentAroundLocation(double lat, double lng, double radius, ITApiCallback<List<ITState>> callback) {
        mService.getCurrentAroundLocation(lat, lng, radius).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the current states of the parts with the given refs.
     *
     * @param partRefs the ITParts' external refs
     * @param status   the status of the states you want to retrieve if you want to filter by status
     */
    public void getCurrentByParts(String[] partRefs, String status, ITApiCallback<List<ITState>> callback) {
        mService.getCurrentByAssets("part", partRefs, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the current states of the sites with the given refs.
     *
     * @param siteRefs the ITSites' external refs
     */
    public void getCurrentBySites(String[] siteRefs, ITApiCallback<List<ITState>> callback) {
        getCurrentBySites(siteRefs, null, callback);
    }

    /**
     * Retrieves the current states of the sites with the given refs.
     *
     * @param siteRefs the ITSites' external refs
     * @param status   the status of the states you want to retrieve if you want to filter by status
     */
    public void getCurrentBySites(String[] siteRefs, String status, ITApiCallback<List<ITState>> callback) {
        mService.getCurrentByAssets("site", siteRefs, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the current states of the streams with the given IDs.
     *
     * @param streamIds the ITStreams' IDs
     */
    public void getCurrentByStreams(String[] streamIds, ITApiCallback<List<ITState>> callback) {
        getCurrentByStreams(streamIds, null, callback);
    }

    /**
     * Retrieves the current states of the streams with the given IDs.
     *
     * @param streamIds the ITStreams' IDS
     * @param status    the status of the states you want to retrieve if you want to filter by status
     */
    public void getCurrentByStreams(String[] streamIds, String status, ITApiCallback<List<ITState>> callback) {
        mService.getCurrentByStreams(streamIds, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the history of the states during the given period, for the parts with the given refs.
     *
     * @param partRefs the ITParts' external refs
     * @param from     the start time in ms
     * @param to       the end time in ms
     */
    public void getHistoryByParts(String[] partRefs, long from, long to, ITApiCallback<List<ITState>> callback) {
        getHistoryByParts(partRefs, from, to, null, callback);
    }

    /**
     * Retrieves the history of the states during the given period, for the parts with the given refs.
     *
     * @param partRefs the ITParts' external refs
     * @param from     the start time in ms
     * @param to       the end time in ms
     * @param status   the status of the states you want to retrieve if you want to filter by status
     */
    public void getHistoryByParts(String[] partRefs, long from, long to, String status, ITApiCallback<List<ITState>> callback) {
        mService.getHistoryByAssets("part", partRefs, from, to, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the history of the states during the given period, for the sites with the given refs.
     *
     * @param siteRefs the ITSites' external refs
     * @param from     the start time in ms
     * @param to       the end time in ms
     */
    public void getHistoryBySites(String[] siteRefs, long from, long to, ITApiCallback<List<ITState>> callback) {
        getHistoryBySites(siteRefs, from, to, null, callback);
    }

    /**
     * Retrieves the history of the states during the given period, for the sites with the given refs.
     *
     * @param siteRefs the ITSites' external refs
     * @param from     the start time in ms
     * @param to       the end time in ms
     * @param status   the status of the states you want to retrieve if you want to filter by status
     */
    public void getHistoryBySites(String[] siteRefs, long from, long to, String status, ITApiCallback<List<ITState>> callback) {
        mService.getHistoryByAssets("site", siteRefs, from, to, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the history of the states of during the given period, for the streams with the given IDs.
     *
     * @param streamIds the ITStreams' IDs
     * @param from      the start time in ms
     * @param to        the end time in ms
     */
    public void getHistoryByStreams(String[] streamIds, long from, long to, ITApiCallback<List<ITState>> callback) {
        getHistoryByStreams(streamIds, from, to, null, callback);
    }

    /**
     * Retrieves the history of the states of during the given period, for the streams with the given IDs.
     *
     * @param streamIds the ITStreams' IDS
     * @param from      the start time in ms
     * @param to        the end time in ms
     * @param status    the status of the states you want to retrieve if you want to filter by status
     */
    public void getHistoryByStreams(String[] streamIds, long from, long to, String status, ITApiCallback<List<ITState>> callback) {
        mService.getHistoryByStreams(streamIds, from, to, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves all the state templates.
     */
    public void getTemplates(final ITApiCallback<List<ITStateTemplate>> callback) {
        mService.getTemplates(false).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("v1/states/current/domain")
        Call<List<ITState>> getCurrent(@Query("status") String status);

        @GET("v1/states/current/position")
        Call<List<ITState>> getCurrentAroundLocation(@Query("lat") double lat, @Query("lng") double lng, @Query("distance") double distance);

        @GET("v1/states/current/assets")
        Call<List<ITState>> getCurrentByAssets(@Query("assetType") String assetType, @Query("assetIds") String[] assetIds, @Query("status") String status);

        @GET("v1/states/current/streams")
        Call<List<ITState>> getCurrentByStreams(@Query("streamIds") String[] assetIds, @Query("status") String status);

        @GET("v1/states/history/assets")
        Call<List<ITState>> getHistoryByAssets(@Query("assetType") String assetType, @Query("assetIds") String[] assetIds, @Query("fromTimestamp") long from, @Query("toTimestamp") long to, @Query("status") String status);

        @GET("v1/states/history/streams")
        Call<List<ITState>> getHistoryByStreams(@Query("streamIds") String[] assetIds, @Query("fromTimestamp") long from, @Query("toTimestamp") long to, @Query("status") String status);

        @GET("v1/states/templates")
        Call<List<ITStateTemplate>> getTemplates(@Query("summary") boolean summary);
    }
}
