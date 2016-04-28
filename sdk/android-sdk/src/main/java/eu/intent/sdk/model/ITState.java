package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

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
import retrofit2.http.Query;

/**
 * The state of a part or site at a given time. A state is defined by the implementation of a template, i.e. a function with some parameters.
 * <p/>
 * For example we can define a template "overheating", based on the function "threshold crossing", on the activity "average temperature". The states of this template would be "ON" or "OFF".
 * Then we define an observer, which is an implementation of this template with concrete parameters to the function, on a given site, for example "Site A" must not have an average temperature higher than "23°C".
 * At any time, Site A will have a state for this observer, either "ON" or "OFF" depending on its average temperature.
 *
 * @see ITStateTemplate
 */
public class ITState implements Parcelable {
    public static final Parcelable.Creator<ITState> CREATOR = new Parcelable.Creator<ITState>() {
        public ITState createFromParcel(Parcel source) {
            return new ITState(source);
        }

        public ITState[] newArray(int size) {
            return new ITState[size];
        }
    };

    private static Service sService;

    public String domain;
    public long lastChange;
    public long lastUpdate;
    public String observerId;
    public String status;
    public String statusColor;
    public String statusDefault;
    public String[] statusEnum;
    public ITStream stream;
    public String templateId;
    /**
     * Date of alert.
     */
    @SerializedName("creationDate")
    public long time;
    public long validityDuration;
    /**
     * When the state was created. Typically, data reception date
     */
    @SerializedName("receptionDate")
    public long creationTime;

    transient public ITStateParams params;
    transient public Map<String, String> texts = new HashMap<>();

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITState() {
    }

    protected ITState(Parcel in) {
        domain = in.readString();
        lastChange = in.readLong();
        lastUpdate = in.readLong();
        observerId = in.readString();
        String paramsClass = in.readString();
        try {
            params = in.readParcelable(Class.forName(paramsClass).getClassLoader());
        } catch (ClassNotFoundException ignored) {
        }
        status = in.readString();
        statusColor = in.readString();
        statusDefault = in.readString();
        statusEnum = in.createStringArray();
        stream = in.readParcelable(ITStream.class.getClassLoader());
        templateId = in.readString();
        Bundle textsBundle = in.readBundle();
        for (String key : textsBundle.keySet()) {
            texts.put(key, textsBundle.getString(key));
        }
        time = in.readLong();
        validityDuration = in.readLong();
        creationTime = in.readLong();
        custom = in.readBundle();
    }

    private static Service getServiceInstance(Context context) {
        if (sService == null) {
            sService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
        }
        return sService;
    }

    /**
     * Retrieves the current states of the user's domain.
     */
    public static void getCurrent(Context context, ITApiCallback<List<ITState>> callback) {
        getCurrent(context, null, callback);
    }

    /**
     * Retrieves the current states of the user's domain.
     *
     * @param status the status of the states you want to retrieve if you want to filter by status
     */
    public static void getCurrent(Context context, String status, ITApiCallback<List<ITState>> callback) {
        getServiceInstance(context).getCurrent(status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the current states of the parts with the given refs.
     *
     * @param partRefs the ITParts' external refs
     */
    public static void getCurrentByParts(Context context, String[] partRefs, ITApiCallback<List<ITState>> callback) {
        getCurrentByParts(context, partRefs, null, callback);
    }

    /**
     * Retrieves the current states around the given location.
     *
     * @param lat    the location latitude
     * @param lng    the location longitude
     * @param radius the radius in meters around the location
     */
    public static void getCurrentAroundLocation(Context context, double lat, double lng, double radius, ITApiCallback<List<ITState>> callback) {
        getServiceInstance(context).getCurrentAroundLocation(lat, lng, radius).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the current states of the parts with the given refs.
     *
     * @param partRefs the ITParts' external refs
     * @param status   the status of the states you want to retrieve if you want to filter by status
     */
    public static void getCurrentByParts(Context context, String[] partRefs, String status, ITApiCallback<List<ITState>> callback) {
        getServiceInstance(context).getCurrentByAssets("part", partRefs, false, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the current states of the sites with the given refs.
     *
     * @param siteRefs         the ITSites' external refs
     * @param withLinkedAssets if true, the API will fetch the states of the site and its assets (parts, equipments), otherwise it will fetch the states for the site only
     */
    public static void getCurrentBySites(Context context, String[] siteRefs, boolean withLinkedAssets, ITApiCallback<List<ITState>> callback) {
        getCurrentBySites(context, siteRefs, withLinkedAssets, null, callback);
    }

    /**
     * Retrieves the current states of the sites with the given refs.
     *
     * @param siteRefs         the ITSites' external refs
     * @param withLinkedAssets if true, the API will fetch the states of the site and its assets (parts, equipments), otherwise it will fetch the states for the site only
     * @param status           the status of the states you want to retrieve if you want to filter by status
     */
    public static void getCurrentBySites(Context context, String[] siteRefs, boolean withLinkedAssets, String status, ITApiCallback<List<ITState>> callback) {
        getServiceInstance(context).getCurrentByAssets("site", siteRefs, withLinkedAssets, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the current states of the streams with the given IDs.
     *
     * @param streamIds the ITStreams' IDs
     */
    public static void getCurrentByStreams(Context context, String[] streamIds, ITApiCallback<List<ITState>> callback) {
        getCurrentByStreams(context, streamIds, null, callback);
    }

    /**
     * Retrieves the current states of the streams with the given IDs.
     *
     * @param streamIds the ITStreams' IDS
     * @param status    the status of the states you want to retrieve if you want to filter by status
     */
    public static void getCurrentByStreams(Context context, String[] streamIds, String status, ITApiCallback<List<ITState>> callback) {
        getServiceInstance(context).getCurrentByStreams(streamIds, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the history of the states during the given period, for the parts with the given refs.
     *
     * @param partRefs the ITParts' external refs
     * @param from     the start time in ms
     * @param to       the end time in ms
     */
    public static void getHistoryByParts(Context context, String[] partRefs, long from, long to, ITApiCallback<List<ITState>> callback) {
        getHistoryByParts(context, partRefs, from, to, null, callback);
    }

    /**
     * Retrieves the history of the states during the given period, for the parts with the given refs.
     *
     * @param partRefs the ITParts' external refs
     * @param from     the start time in ms
     * @param to       the end time in ms
     * @param status   the status of the states you want to retrieve if you want to filter by status
     */
    public static void getHistoryByParts(Context context, String[] partRefs, long from, long to, String status, ITApiCallback<List<ITState>> callback) {
        getServiceInstance(context).getHistoryByAssets("part", partRefs, from, to, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the history of the states during the given period, for the sites with the given refs.
     *
     * @param siteRefs the ITSites' external refs
     * @param from     the start time in ms
     * @param to       the end time in ms
     */
    public static void getHistoryBySites(Context context, String[] siteRefs, long from, long to, ITApiCallback<List<ITState>> callback) {
        getHistoryBySites(context, siteRefs, from, to, null, callback);
    }

    /**
     * Retrieves the history of the states during the given period, for the sites with the given refs.
     *
     * @param siteRefs the ITSites' external refs
     * @param from     the start time in ms
     * @param to       the end time in ms
     * @param status   the status of the states you want to retrieve if you want to filter by status
     */
    public static void getHistoryBySites(Context context, String[] siteRefs, long from, long to, String status, ITApiCallback<List<ITState>> callback) {
        getServiceInstance(context).getHistoryByAssets("site", siteRefs, from, to, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Retrieves the history of the states of during the given period, for the streams with the given IDs.
     *
     * @param streamIds the ITStreams' IDs
     * @param from      the start time in ms
     * @param to        the end time in ms
     */
    public static void getHistoryByStreams(Context context, String[] streamIds, long from, long to, ITApiCallback<List<ITState>> callback) {
        getHistoryByStreams(context, streamIds, from, to, null, callback);
    }

    /**
     * Retrieves the history of the states of during the given period, for the streams with the given IDs.
     *
     * @param streamIds the ITStreams' IDS
     * @param from      the start time in ms
     * @param to        the end time in ms
     * @param status    the status of the states you want to retrieve if you want to filter by status
     */
    public static void getHistoryByStreams(Context context, String[] streamIds, long from, long to, String status, ITApiCallback<List<ITState>> callback) {
        getServiceInstance(context).getHistoryByStreams(streamIds, from, to, status).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Returns true if the current status is the default status, false otherwise.
     */
    public boolean isInDefaultStatus() {
        return TextUtils.equals(status, statusDefault);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(domain);
        dest.writeLong(lastChange);
        dest.writeLong(lastUpdate);
        dest.writeString(observerId);
        dest.writeString(params.getClass().getCanonicalName());
        dest.writeParcelable((Parcelable) params, flags);
        dest.writeString(status);
        dest.writeString(statusColor);
        dest.writeString(statusDefault);
        dest.writeStringArray(statusEnum);
        dest.writeParcelable(stream, flags);
        dest.writeString(templateId);
        Bundle textsBundle = new Bundle();
        for (Map.Entry<String, String> entry : texts.entrySet()) {
            textsBundle.putString(entry.getKey(), entry.getValue());
        }
        dest.writeBundle(textsBundle);
        dest.writeLong(time);
        dest.writeLong(validityDuration);
        dest.writeLong(creationTime);
        dest.writeBundle(custom);
    }

    private interface Service {
        @GET("v1/states/current/domain")
        Call<List<ITState>> getCurrent(@Query("status") String status);

        @GET("v1/states/current/position")
        Call<List<ITState>> getCurrentAroundLocation(@Query("lat") double lat, @Query("lng") double lng, @Query("distance") double distance);

        @GET("v1/states/current/assets")
        Call<List<ITState>> getCurrentByAssets(@Query("assetType") String assetType, @Query("assetIds") String[] assetIds, @Query("withLinkedAssets") boolean withLinkedAssets, @Query("status") String status);

        @GET("v1/states/current/streams")
        Call<List<ITState>> getCurrentByStreams(@Query("streamIds") String[] assetIds, @Query("status") String status);

        @GET("v1/states/history/assets")
        Call<List<ITState>> getHistoryByAssets(@Query("assetType") String assetType, @Query("assetIds") String[] assetIds, @Query("fromTimestamp") long from, @Query("toTimestamp") long to, @Query("status") String status);

        @GET("v1/states/history/streams")
        Call<List<ITState>> getHistoryByStreams(@Query("streamIds") String[] assetIds, @Query("fromTimestamp") long from, @Query("toTimestamp") long to, @Query("status") String status);
    }

    public static class Deserializer implements JsonDeserializer<ITState> {
        @Override
        public ITState deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ITStream.class, new ITStream.Deserializer())
                    .create();
            ITState state = gson.fromJson(json, typeOfT);
            JsonObject jsonObject = json.getAsJsonObject();
            JsonObject jsonParams = jsonObject.getAsJsonObject("params");
            if (jsonParams.has("timetables") && jsonParams.get("timetables").isJsonArray()) {
                state.params = gson.fromJson(jsonParams, ITStateParamsThresholds.class);
            }
            JsonObject jsonTexts = jsonObject.getAsJsonObject("texts");
            Type mapType = new TypeToken<Map<String, String>>() {
            }.getType();
            state.texts = gson.fromJson(jsonTexts, mapType);
            if (state.time == 0 && jsonObject.has("lastUpdate")) {
                state.time = jsonObject.get("lastUpdate").getAsLong();
            }
            if (state.texts == null) state.texts = new HashMap<>();
            return state;
        }
    }
}
