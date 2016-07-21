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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @SerializedName("activityId")
    public String id;
    @SerializedName("activityDescription")
    public String description;
    @SerializedName("activityLabel")
    public String label;

    transient public Map<String, String> tags = new ConcurrentHashMap<>();

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! It is not saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITActivity() {
        // Needed by Retrofit
    }

    protected ITActivity(Parcel in) {
        id = in.readString();
        description = in.readString();
        label = in.readString();
        Bundle tagsBundle = in.readBundle();
        for (String tag : tagsBundle.keySet()) {
            tags.put(tag, tagsBundle.getString(tag));
        }
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
    }

    @Override
    public String toString() {
        return label;
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
