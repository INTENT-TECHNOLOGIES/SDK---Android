package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.util.ITDateUtils;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * A news, published by the responsible of a site.
 */
public class ITNews implements Parcelable {
    public static final Parcelable.Creator<ITNews> CREATOR = new Parcelable.Creator<ITNews>() {
        public ITNews createFromParcel(Parcel source) {
            return new ITNews(source);
        }

        public ITNews[] newArray(int size) {
            return new ITNews[size];
        }
    };

    private static Service sService;

    @SerializedName("newsId")
    public String id;
    public long timestamp;

    transient public String eventLocation;
    transient public long eventTimestamp;
    transient public String text;
    transient public String title;
    transient public Type type;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITNews() {
    }

    protected ITNews(Parcel in) {
        eventLocation = in.readString();
        eventTimestamp = in.readLong();
        id = in.readString();
        text = in.readString();
        timestamp = in.readLong();
        title = in.readString();
        int tmpType = in.readInt();
        type = tmpType == -1 ? null : Type.values()[tmpType];
        custom = in.readBundle();
    }

    /**
     * Gets the news available to the authenticated user.
     */
    public static void get(Context context, ITApiCallback<List<ITNews>> callback) {
        get(context, 0, System.currentTimeMillis(), callback);
    }

    /**
     * Gets the news available to the authenticated user.
     *
     * @param since the oldest date you're interested in, in ms
     */
    public static void get(Context context, long since, ITApiCallback<List<ITNews>> callback) {
        get(context, since, System.currentTimeMillis(), callback);
    }

    /**
     * Gets the news available to the authenticated user.
     *
     * @param startTime the oldest date you're interested in, in ms
     * @param endTime   the latest date you're interested in, in ms
     */
    public static void get(Context context, long startTime, long endTime, ITApiCallback<List<ITNews>> callback) {
        get(context, null, startTime, endTime, callback);
    }

    /**
     * Gets the news available to the authenticated user.
     *
     * @param types     an array of ITNews.Type
     * @param startTime the oldest date you're interested in, in ms
     * @param endTime   the latest date you're interested in, in ms
     */
    public static void get(Context context, Type[] types, long startTime, long endTime, ITApiCallback<List<ITNews>> callback) {
        String typesString = TextUtils.join(",", types != null && types.length > 0 ? types : Type.values());
        String startTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(startTime));
        String endTimeIso8601 = ITDateUtils.formatDateIso8601(new Date(endTime));
        getServiceInstance(context).get(typesString, startTimeIso8601, endTimeIso8601).enqueue(new ProxyCallback<>(callback));
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
        dest.writeString(eventLocation);
        dest.writeLong(eventTimestamp);
        dest.writeString(id);
        dest.writeString(text);
        dest.writeLong(timestamp);
        dest.writeString(title);
        dest.writeInt(type == null ? -1 : type.ordinal());
        dest.writeBundle(custom);
    }

    public enum Type {
        EVENT,
        INFO,
        TIP,
        UNKNOWN;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    private interface Service {
        @GET("residentservices/v1/news/mine")
        Call<List<ITNews>> get(@Query("category") String categories, @Query("startTime") String startTime, @Query("endTime") String endTime);
    }

    public static class Deserializer implements JsonDeserializer<ITNews> {
        @Override
        public ITNews deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ITNews news = new Gson().fromJson(json, typeOfT);
            try {
                news.type = Type.valueOf(json.getAsJsonObject().get("category").getAsString().toUpperCase());
            } catch (IllegalArgumentException e) {
                news.type = Type.UNKNOWN;
            }
            JsonObject content = json.getAsJsonObject().getAsJsonObject("content");
            if (content.has("eventLocation")) {
                news.eventLocation = content.get("eventLocation").getAsString();
            }
            if (content.has("eventDate")) {
                news.eventTimestamp = content.get("eventDate").getAsLong();
            }
            news.text = content.get("description").getAsString();
            news.title = content.get("title").getAsString();
            return news;
        }
    }
}
