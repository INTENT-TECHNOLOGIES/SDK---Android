package eu.intent.sdk.model;

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

import java.util.Locale;

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
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITNews() {
        // Needed by Retrofit
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
            return name().toLowerCase(Locale.US);
        }
    }

    public static class Deserializer implements JsonDeserializer<ITNews> {
        @Override
        public ITNews deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ITNews news = new Gson().fromJson(json, typeOfT);
            try {
                news.type = Type.valueOf(json.getAsJsonObject().get("category").getAsString().toUpperCase(Locale.US));
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
