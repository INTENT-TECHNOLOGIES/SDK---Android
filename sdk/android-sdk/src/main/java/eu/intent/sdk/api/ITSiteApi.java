package eu.intent.sdk.api;

import android.content.Context;

import java.util.List;
import java.util.Locale;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITSite;
import eu.intent.sdk.model.ITSiteList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A wrapper to call the Site API.
 */
public class ITSiteApi {
    private Service mService;

    public ITSiteApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Retrieves the site with the given reference.
     *
     * @param siteRef the site reference
     */
    public void get(String siteRef, ITApiCallback<ITSite> callback) {
        mService.get(siteRef, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the sites of the user's domain.
     *
     * @param page the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public void get(int page, ITApiCallback<ITSiteList> callback) {
        mService.get(null, page, Service.MAX_COUNT, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the sites of the user's domain, matching the given address criteria.
     *
     * @param postalCode  the postal code of the searched city
     * @param city        the name of the searched city
     * @param countryCode the code of the searched country (format: "FR")
     */
    public void getByAddress(String postalCode, String city, String countryCode, ITApiCallback<ITSiteList> callback) {
        mService.getByAddress(postalCode, city, countryCode, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the sites of the user's domain, matching the given query.
     *
     * @param query a query string to filter the results
     */
    public void search(String query, ITApiCallback<ITSiteList> callback) {
        mService.get(query, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the IDs of the sites around the given location.
     *
     * @param lat         latitude
     * @param lng         longitude
     * @param maxDistance search the sites in this maximum distance around the given location, in meters
     */
    public void getSiteIdsAroundLocation(double lat, double lng, int maxDistance, ITApiCallback<List<String>> callback) {
        mService.getSiteIdsAroundLocation(lat, lng, maxDistance).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the sites around the given location.
     *
     * @param lat         latitude
     * @param lng         longitude
     * @param maxDistance search the sites in this maximum distance around the given location, in meters
     */
    public void getSitesAroundLocation(double lat, double lng, int maxDistance, ITApiCallback<List<ITSite>> callback) {
        mService.getSitesAroundLocation(lat, lng, maxDistance).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        int MAX_COUNT = 50;

        @GET("datahub/v1/sites/near?onlyIds=true")
        Call<List<String>> getSiteIdsAroundLocation(@Query("lat") double lat, @Query("lng") double lng, @Query("maxDistance") int maxDistance);

        @GET("datahub/v1/sites/near")
        Call<List<ITSite>> getSitesAroundLocation(@Query("lat") double lat, @Query("lng") double lng, @Query("maxDistance") int maxDistance);

        @GET("datahub/v1/sites/{externalRef}")
        Call<ITSite> get(@Path("externalRef") String siteRef, @Query("lang") String lang);

        @GET("datahub/v1/sites")
        Call<ITSiteList> get(@Query("query") String query, @Query("page") int page, @Query("countByPage") int count, @Query("lang") String lang);

        @GET("datahub/v1/sites")
        Call<ITSiteList> getByAddress(@Query("filters[zip]") String postalCode, @Query("filters[city]") String city, @Query("filters[country]") String countryCode, @Query("page") int page, @Query("countByPage") int count, @Query("lang") String lang);
    }
}
