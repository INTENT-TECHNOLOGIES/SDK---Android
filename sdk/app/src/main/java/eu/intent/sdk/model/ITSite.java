package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Locale;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A physical site (building, residence, house).
 */
public class ITSite implements Parcelable {
    public static final Parcelable.Creator<ITSite> CREATOR = new Parcelable.Creator<ITSite>() {
        public ITSite createFromParcel(Parcel source) {
            return new ITSite(source);
        }

        public ITSite[] newArray(int size) {
            return new ITSite[size];
        }
    };

    private static Service sService;

    public ITAddress address;
    public String externalRef;
    public String id;
    public String label;
    public String owner;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITSite() {
    }

    protected ITSite(Parcel in) {
        address = in.readParcelable(ITAddress.class.getClassLoader());
        externalRef = in.readString();
        id = in.readString();
        label = in.readString();
        owner = in.readString();
        custom = in.readBundle();
    }

    /**
     * Retrieves the site with the given reference.
     *
     * @param siteRef the site reference
     */
    public static void get(Context context, String siteRef, ITApiCallback<ITSite> callback) {
        getServiceInstance(context).get(siteRef, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the sites of the user's domain.
     *
     * @param page the results are paginated, you need to precise the page you want to load (page index starts at 1)
     */
    public static void get(Context context, int page, ITApiCallback<ITSiteList> callback) {
        getServiceInstance(context).get(null, page, Service.MAX_COUNT, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the sites of the user's domain, matching the given address criteria.
     *
     * @param postalCode  the postal code of the searched city
     * @param city        the name of the searched city
     * @param countryCode the code of the searched country (format: "FR")
     */
    public static void getByAddress(Context context, String postalCode, String city, String countryCode, ITApiCallback<ITSiteList> callback) {
        getServiceInstance(context).getByAddress(postalCode, city, countryCode, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the sites of the user's domain, matching the given query.
     *
     * @param query a query string to filter the results
     */
    public static void search(Context context, String query, ITApiCallback<ITSiteList> callback) {
        getServiceInstance(context).get(query, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the IDs of the sites around the given location.
     *
     * @param lat         latitude
     * @param lng         longitude
     * @param maxDistance search the sites in this maximum distance around the given location, in meters
     */
    public static void getSiteIdsAroundLocation(Context context, double lat, double lng, int maxDistance, ITApiCallback<List<String>> callback) {
        getServiceInstance(context).getSiteIdsAroundLocation(lat, lng, maxDistance).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the sites around the given location.
     *
     * @param lat         latitude
     * @param lng         longitude
     * @param maxDistance search the sites in this maximum distance around the given location, in meters
     */
    public static void getSitesAroundLocation(Context context, double lat, double lng, int maxDistance, ITApiCallback<List<ITSite>> callback) {
        getServiceInstance(context).getSitesAroundLocation(lat, lng, maxDistance).enqueue(new ProxyCallback<>(callback));
    }

    private static Service getServiceInstance(Context context) {
        if (sService == null) {
            sService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
        }
        return sService;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(address, 0);
        dest.writeString(externalRef);
        dest.writeString(id);
        dest.writeString(label);
        dest.writeString(owner);
        dest.writeBundle(custom);
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
