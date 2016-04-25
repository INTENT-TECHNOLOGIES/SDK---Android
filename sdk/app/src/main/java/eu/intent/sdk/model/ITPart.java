package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
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
 * A part of a site, either common (eg boiler installRoom) or private (an habitation).
 */
public class ITPart implements Parcelable {
    public static final Parcelable.Creator<ITPart> CREATOR = new Parcelable.Creator<ITPart>() {
        public ITPart createFromParcel(Parcel source) {
            return new ITPart(source);
        }

        public ITPart[] newArray(int size) {
            return new ITPart[size];
        }
    };

    private static Service sService;

    public ITAddress address;
    public String door;
    public String externalRef;
    public String id;
    @SerializedName("level")
    public String floor;
    public String label;
    public String owner;
    public ITPortion portion;
    public String siteRef;
    public List<ITUser> users = new ArrayList<>();

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITPart() {
    }

    protected ITPart(Parcel in) {
        address = in.readParcelable(ITAddress.class.getClassLoader());
        door = in.readString();
        externalRef = in.readString();
        id = in.readString();
        floor = in.readString();
        label = in.readString();
        owner = in.readString();
        int tmpPortion = in.readInt();
        portion = tmpPortion == -1 ? null : ITPortion.values()[tmpPortion];
        siteRef = in.readString();
        users = new ArrayList<>();
        in.readList(users, ITUser.class.getClassLoader());
        custom = in.readBundle();
    }

    /**
     * Retrieves the parts of the user's domain.
     *
     * @param page        the results are paginated, you need to precise the page you want to load (page index starts at 1)
     * @param countByPage the results are paginated, you need to precise the number of results you want by page (max 50)
     */
    public static void get(Context context, int page, int countByPage, ITApiCallback<ITPartList> callback) {
        getServiceInstance(context).getBySiteRef(null, null, page, countByPage, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the parts of the user's domain, matching the given query.
     *
     * @param query a query string to filter the results
     */
    public static void get(Context context, String query, ITApiCallback<ITPartList> callback) {
        getServiceInstance(context).getBySiteRef(null, query, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the parts of the given site.
     *
     * @param siteId the ITSite's ID
     */
    public static void getBySiteId(Context context, String siteId, ITApiCallback<ITPartList> callback) {
        getServiceInstance(context).getBySiteId(siteId, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the parts of the given site, matching the given query.
     *
     * @param siteId the ITSite's ID
     * @param query  a query string to filter the results
     */
    public static void getBySiteId(Context context, String siteId, String query, ITApiCallback<ITPartList> callback) {
        getServiceInstance(context).getBySiteId(siteId, query, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the parts of the given site.
     *
     * @param siteRef the ITSite's external ref
     */
    public static void getBySiteRef(Context context, String siteRef, ITApiCallback<ITPartList> callback) {
        getServiceInstance(context).getBySiteRef(siteRef, null, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the parts of the given site, matching the given query.
     *
     * @param siteRef the ITSite's external ref
     * @param query   a query string to filter the results
     */
    public static void getBySiteRef(Context context, String siteRef, String query, ITApiCallback<ITPartList> callback) {
        getServiceInstance(context).getBySiteRef(siteRef, query, 1, 0, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the user part, works only for home users.
     * The service returns a 404 HTML code if the user isn't linked to a part.
     */
    public static void getMyPart(Context context, ITApiCallback<ITPart> callback) {
        getByRef(context, "mine", callback);
    }

    /**
     * Retrieves the part with the given ID.
     *
     * @param partId the part ID
     */
    public static void getById(Context context, String partId, ITApiCallback<ITPart> callback) {
        getServiceInstance(context).get(partId, true, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the part with the given reference.
     *
     * @param partRef the part reference
     */
    public static void getByRef(Context context, String partRef, ITApiCallback<ITPart> callback) {
        getServiceInstance(context).get(partRef, false, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
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
        dest.writeString(door);
        dest.writeString(externalRef);
        dest.writeString(id);
        dest.writeString(floor);
        dest.writeString(label);
        dest.writeString(owner);
        dest.writeInt(portion == null ? -1 : portion.ordinal());
        dest.writeString(siteRef);
        dest.writeList(users);
        dest.writeBundle(custom);
    }

    public enum ITPortion {
        @SerializedName("commonPortion")COMMON,
        @SerializedName("privatePortion")PRIVATE,
        UNKNOWN
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
