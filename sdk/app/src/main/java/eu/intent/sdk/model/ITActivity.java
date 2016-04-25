package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * An activity is something that can be measured or trigger events, for example temperature, electricity consumption, or lifts.
 */
public class ITActivity implements Parcelable {
    public static final Creator<ITActivity> CREATOR = new Creator<ITActivity>() {
        public ITActivity createFromParcel(Parcel source) {
            return new ITActivity(source);
        }

        public ITActivity[] newArray(int size) {
            return new ITActivity[size];
        }
    };

    private static Service sService;

    @SerializedName("activityId")
    public String id;
    @SerializedName("activityDescription")
    public String description;
    @SerializedName("activityLabel")
    public String label;

    transient public Map<String, String> tags = new HashMap<>();

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITActivity() {
    }

    protected ITActivity(Parcel in) {
        id = in.readString();
        description = in.readString();
        label = in.readString();
        Bundle tagsBundle = in.readBundle();
        for (String tag : tagsBundle.keySet()) {
            tags.put(tag, tagsBundle.getString(tag));
        }
        custom = in.readBundle();
    }

    /**
     * Retrieves the activities list.
     */
    public static void get(Context context, ITApiCallback<ITActivityList> callback) {
        getServiceInstance(context).get(Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities grouped by category. The result will be a list of ITActivityCategory, containing the activities IDs.
     */
    public static void getGroupedByCategory(Context context, ITApiCallback<List<ITActivityCategory>> callback) {
        getServiceInstance(context).getByCategory().enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activity with the given ID.
     *
     * @param id the activity ID
     */
    public static void getById(Context context, String id, ITApiCallback<ITActivity> callback) {
        getServiceInstance(context).get(id, Locale.getDefault().getLanguage()).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities of a given part. The callback returns the activity keys.
     *
     * @param partRef the ITPart's external ref
     */
    public static void getByPartRef(Context context, String partRef, ITApiCallback<ITActivityKeys> callback) {
        getServiceInstance(context).getByPart(partRef, false).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities of a given part by Intent Id. The callback returns the activity keys.
     *
     * @param partId the ITPart's Intent id
     */
    public static void getByPartId(Context context, String partId, ITApiCallback<ITActivityKeys> callback) {
        getServiceInstance(context).getByPart(partId, true).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities of a given site. The callback returns the activity keys.
     *
     * @param siteRef          the ITSite's external ref
     * @param withLinkedAssets if true, the API will fetch the activities of the site and its assets (parts, equipments), otherwise it will fetch the activities for the site only
     */
    public static void getBySiteRef(Context context, String siteRef, boolean withLinkedAssets, ITApiCallback<ITActivityKeys> callback) {
        getServiceInstance(context).getBySite(siteRef, false, withLinkedAssets).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the activities of a given site by Intent Id. The callback returns the activity keys.
     *
     * @param siteId           the ITSite's Intent id
     * @param withLinkedAssets if true, the API will fetch the activities of the site and its assets (parts, equipments), otherwise it will fetch the activities for the site only
     */
    public static void getBySiteId(Context context, String siteId, boolean withLinkedAssets, ITApiCallback<ITActivityKeys> callback) {
        getServiceInstance(context).getBySite(siteId, true, withLinkedAssets).enqueue(new ProxyCallback<>(callback));
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
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(label);
        Bundle tagsBundle = new Bundle();
        for (Map.Entry<String, String> tag : tags.entrySet()) {
            tagsBundle.putString(tag.getKey(), tag.getValue());
        }
        dest.writeBundle(tagsBundle);
        dest.writeBundle(custom);
    }

    @Override
    public String toString() {
        return label;
    }

    private interface Service {
        @GET("datahub/v1/activities")
        Call<ITActivityList> get(@Query("lang") String lang);

        @GET("datahub/v1/activities/{activityKey}")
        Call<ITActivity> get(@Path("activityKey") String id, @Query("lang") String lang);

        @GET("datahub/v1/parts/{externalRef}/activities")
        Call<ITActivityKeys> getByPart(@Path("externalRef") String partRef, @Query("byId") boolean byId);

        @GET("datahub/v1/sites/{externalRef}/activities")
        Call<ITActivityKeys> getBySite(@Path("externalRef") String siteRef, @Query("byId") boolean byId, @Query("withLinkedAssets") boolean withLinkedAssets);

        @GET("datahub/v1/themes")
        Call<List<ITActivityCategory>> getByCategory();
    }

    public static class Deserializer implements JsonDeserializer<ITActivity> {
        @Override
        public ITActivity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new Gson();
            ITActivity activity = gson.fromJson(json, typeOfT);
            JsonObject jsonTags = json.getAsJsonObject().getAsJsonObject("tags");
            Type mapType = new TypeToken<Map<String, String>>() {
            }.getType();
            activity.tags = gson.fromJson(jsonTags, mapType);
            if (activity.tags == null) activity.tags = new HashMap<>();
            return activity;
        }
    }
}
