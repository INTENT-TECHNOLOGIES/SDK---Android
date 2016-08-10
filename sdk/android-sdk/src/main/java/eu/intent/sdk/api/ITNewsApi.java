package eu.intent.sdk.api;

import android.content.Context;
import android.text.TextUtils;

import java.util.Date;
import java.util.List;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITNews;
import eu.intent.sdk.util.ITDateUtils;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * A wrapper to call the News API.
 */
public class ITNewsApi {
    private Service mService;

    public ITNewsApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Gets the news available to the authenticated user.
     */
    public void get(ITApiCallback<List<ITNews>> callback) {
        get(0, System.currentTimeMillis(), callback);
    }

    /**
     * Gets the news available to the authenticated user.
     *
     * @param since the oldest date you're interested in, in ms
     */
    public void get(long since, ITApiCallback<List<ITNews>> callback) {
        get(since, System.currentTimeMillis(), callback);
    }

    /**
     * Gets the news available to the authenticated user.
     *
     * @param startTime the oldest date you're interested in, in ms
     * @param endTime   the latest date you're interested in, in ms
     */
    public void get(long startTime, long endTime, ITApiCallback<List<ITNews>> callback) {
        get(null, startTime, endTime, callback);
    }

    /**
     * Gets the news available to the authenticated user.
     *
     * @param types     an array of ITNews.Type
     * @param startTime the oldest date you're interested in, in ms
     * @param endTime   the latest date you're interested in, in ms
     */
    public void get(ITNews.Type[] types, long startTime, long endTime, ITApiCallback<List<ITNews>> callback) {
        String typesString = TextUtils.join(",", types != null && types.length > 0 ? types : ITNews.Type.values());
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        mService.get(typesString, startTimeIso8601, endTimeIso8601).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("residentservices/v1/news/mine")
        Call<List<ITNews>> get(@Query("category") String categories, @Query("startTime") String startTime, @Query("endTime") String endTime);
    }
}
