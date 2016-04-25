package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.Map;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A stream is the combination of an activity and a part or a site.
 */
public class ITStream implements Parcelable {
    public static final Parcelable.Creator<ITStream> CREATOR = new Parcelable.Creator<ITStream>() {
        public ITStream createFromParcel(Parcel source) {
            return new ITStream(source);
        }

        public ITStream[] newArray(int size) {
            return new ITStream[size];
        }
    };

    private static Service sService;

    public ITAddress address;
    @SerializedName("streamId")
    public String id;
    public long lastUpdate;
    public ITData lastValue;
    public String owner;

    transient public Map<String, String> tags = new HashMap<>();

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITStream() {
    }

    protected ITStream(Parcel in) {
        address = in.readParcelable(ITAddress.class.getClassLoader());
        id = in.readString();
        lastUpdate = in.readLong();
        lastValue = in.readParcelable(ITData.class.getClassLoader());
        owner = in.readString();
        Bundle tagsBundle = in.readBundle();
        for (String tag : tagsBundle.keySet()) {
            tags.put(tag, tagsBundle.getString(tag));
        }
        custom = in.readBundle();
    }

    /**
     * Retrieves a stream from its ID.
     *
     * @param streamId the ITStream's ID
     */
    public static void get(Context context, String streamId, ITApiCallback<ITStream> callback) {
        getServiceInstance(context).get(streamId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the streams of a given part. The callback returns the stream IDs.
     *
     * @param partId     the ITPart's ID
     * @param activities if you want to filter on activity keys
     */
    public static void getByPartId(Context context, String partId, String[] activities, ITApiCallback<List<String>> callback) {
        getServiceInstance(context).getByPart(partId, true, activities).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the streams of a given part. The callback returns the stream IDs.
     *
     * @param partRef    the ITPart's external ref
     * @param activities if you want to filter on activity keys
     */
    public static void getByPartRef(Context context, String partRef, String[] activities, ITApiCallback<List<String>> callback) {
        getServiceInstance(context).getByPart(partRef, false, activities).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the streams of a given site. The callback returns the stream IDs.
     *
     * @param siteId           the ITSite's ID
     * @param withLinkedAssets if true, the API will fetch the streams of the site and its assets (parts, equipments), otherwise it will fetch the streams for the site only
     * @param activities       if you want to filter on activity keys
     */
    public static void getBySiteId(Context context, String siteId, boolean withLinkedAssets, String[] activities, ITApiCallback<List<String>> callback) {
        getServiceInstance(context).getBySite(siteId, true, withLinkedAssets, activities).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the streams of a given site. The callback returns the stream IDs.
     *
     * @param siteRef          the ITSite's external ref
     * @param withLinkedAssets if true, the API will fetch the streams of the site and its assets (parts, equipments), otherwise it will fetch the streams for the site only
     * @param activities       if you want to filter on activity keys
     */
    public static void getBySiteRef(Context context, String siteRef, boolean withLinkedAssets, String[] activities, ITApiCallback<List<String>> callback) {
        getServiceInstance(context).getBySite(siteRef, false, withLinkedAssets, activities).enqueue(new ProxyCallback<>(callback));
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
        dest.writeString(id);
        dest.writeLong(lastUpdate);
        dest.writeParcelable(lastValue, 0);
        dest.writeString(owner);
        Bundle tagsBundle = new Bundle();
        for (Map.Entry<String, String> tag : tags.entrySet()) {
            tagsBundle.putString(tag.getKey(), tag.getValue());
        }
        dest.writeBundle(tagsBundle);
        dest.writeBundle(custom);
    }

    private interface Service {
        @GET("datahub/v1/streams/{streamId}")
        Call<ITStream> get(@Path("streamId") String streamId);

        @GET("datahub/v1/parts/{externalRef}/streams")
        Call<List<String>> getByPart(@Path("externalRef") String partIdOrRef, @Query("byId") boolean useIdInsteadOfRef, @Query("activityKey") String[] activities);

        @GET("datahub/v1/sites/{externalRef}/streams")
        Call<List<String>> getBySite(@Path("externalRef") String siteIdOrRef, @Query("byId") boolean useIdInsteadOfRef, @Query("withLinkedAssets") boolean withLinkedAssets, @Query("activityKey") String[] activities);
    }

    public static class Deserializer implements JsonDeserializer<ITStream> {
        @Override
        public ITStream deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ITData.class, new ITData.Deserializer())
                    .registerTypeAdapter(ITLocation.class, new ITLocation.Deserializer())
                    .create();
            ITStream stream = gson.fromJson(json, typeOfT);
            JsonObject jsonTags = json.getAsJsonObject().getAsJsonObject("tags");
            Type mapType = new TypeToken<Map<String, String>>() {
            }.getType();
            stream.tags = gson.fromJson(jsonTags, mapType);
            return stream;
        }
    }
}