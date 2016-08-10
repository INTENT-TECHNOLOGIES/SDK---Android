package eu.intent.sdk.api;

import android.content.Context;

import java.util.Locale;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITPart;
import eu.intent.sdk.model.ITPartList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A wrapper to call the Part API.
 */
public class ITPartApi {
    private Service mService;

    public ITPartApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Retrieves the parts of the user's domain.
     *
     * @param page        the results are paginated, you need to precise the page you want to load (page index starts at 1)
     * @param countByPage the results are paginated, you need to precise the number of results you want by page (max 50)
     */
    public void get(int page, int countByPage, ITApiCallback<ITPartList> callback) {
        mService.getBySiteRef(null, null, page, countByPage, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the parts of the user's domain, matching the given query.
     *
     * @param query a query string to filter the results
     */
    public void get(String query, ITApiCallback<ITPartList> callback) {
        mService.getBySiteRef(null, query, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the parts of the given site.
     *
     * @param siteId the ITSite's ID
     */
    public void getBySiteId(String siteId, ITApiCallback<ITPartList> callback) {
        mService.getBySiteId(siteId, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the parts of the given site, matching the given query.
     *
     * @param siteId the ITSite's ID
     * @param query  a query string to filter the results
     */
    public void getBySiteId(String siteId, String query, ITApiCallback<ITPartList> callback) {
        mService.getBySiteId(siteId, query, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the parts of the given site.
     *
     * @param siteRef the ITSite's external ref
     */
    public void getBySiteRef(String siteRef, ITApiCallback<ITPartList> callback) {
        mService.getBySiteRef(siteRef, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the parts of the given site, matching the given query.
     *
     * @param siteRef the ITSite's external ref
     * @param query   a query string to filter the results
     */
    public void getBySiteRef(String siteRef, String query, ITApiCallback<ITPartList> callback) {
        mService.getBySiteRef(siteRef, query, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the user part, works only for home users.
     * The service returns a 404 HTML code if the user isn't linked to a part.
     */
    public void getMyPart(ITApiCallback<ITPart> callback) {
        getByRef("mine", callback);
    }

    /**
     * Retrieves the part with the given ID.
     *
     * @param partId the part ID
     */
    public void getById(String partId, ITApiCallback<ITPart> callback) {
        mService.get(partId, true, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the part with the given reference.
     *
     * @param partRef the part reference
     */
    public void getByRef(String partRef, ITApiCallback<ITPart> callback) {
        mService.get(partRef, false, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("datahub/v1/parts")
        Call<ITPartList> getBySiteRef(@Query("siteExternalRef") String siteRef, @Query("query") String query, @Query("page") int page, @Query("countByPage") int count, @Query("lang") String lang);

        @GET("datahub/v1/parts")
        Call<ITPartList> getBySiteId(@Query("siteId") String siteId, @Query("query") String query, @Query("page") int page, @Query("countByPage") int count, @Query("lang") String lang);

        @GET("datahub/v1/parts/{externalRef}")
        Call<ITPart> get(@Path("externalRef") String partRefOrId, @Query("byId") boolean byId, @Query("lang") String lang);
    }
}
