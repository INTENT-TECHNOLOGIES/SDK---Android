package eu.intent.sdk.api;

import android.content.Context;

import java.util.List;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITStream;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A wrapper to call the Stream API.
 */
public class ITStreamApi {
    private Service mService;

    public ITStreamApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Retrieves a stream from its ID.
     *
     * @param streamId the ITStream's ID
     */
    public void get(String streamId, ITApiCallback<ITStream> callback) {
        mService.get(streamId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the streams of a given part. The callback returns the stream IDs.
     *
     * @param partId     the ITPart's ID
     * @param activities if you want to filter on activity keys
     */
    public void getByPartId(String partId, String[] activities, ITApiCallback<List<String>> callback) {
        mService.getByPart(partId, true, activities).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the streams of a given part. The callback returns the stream IDs.
     *
     * @param partRef    the ITPart's external ref
     * @param activities if you want to filter on activity keys
     */
    public void getByPartRef(String partRef, String[] activities, ITApiCallback<List<String>> callback) {
        mService.getByPart(partRef, false, activities).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the streams of a given site. The callback returns the stream IDs.
     *
     * @param siteId     the ITSite's ID
     * @param activities if you want to filter on activity keys
     */
    public void getBySiteId(String siteId, String[] activities, ITApiCallback<List<String>> callback) {
        mService.getBySite(siteId, true, activities).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the streams of a given site. The callback returns the stream IDs.
     *
     * @param siteRef    the ITSite's external ref
     * @param activities if you want to filter on activity keys
     */
    public void getBySiteRef(String siteRef, String[] activities, ITApiCallback<List<String>> callback) {
        mService.getBySite(siteRef, false, activities).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("datahub/v1/streams/{streamId}")
        Call<ITStream> get(@Path("streamId") String streamId);

        @GET("datahub/v1/parts/{externalRef}/streams")
        Call<List<String>> getByPart(@Path("externalRef") String partIdOrRef, @Query("byId") boolean useIdInsteadOfRef, @Query("activityKey") String... activities);

        @GET("datahub/v1/sites/{externalRef}/streams")
        Call<List<String>> getBySite(@Path("externalRef") String siteIdOrRef, @Query("byId") boolean useIdInsteadOfRef, @Query("activityKey") String... activities);
    }
}
