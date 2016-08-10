package eu.intent.sdk.model;

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
import java.util.Map;

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

    public ITAddress address;
    @SerializedName("streamId")
    public String id;
    public long lastUpdate;
    public ITData lastValue;
    public String owner;

    transient public Map<String, String> tags = new HashMap<>();

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
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