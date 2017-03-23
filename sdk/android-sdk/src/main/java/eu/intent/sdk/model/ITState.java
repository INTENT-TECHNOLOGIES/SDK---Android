package eu.intent.sdk.model;

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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    public long validityExpirationDate;
    /**
     * When the state was created. Typically, data reception date
     */
    @SerializedName("receptionDate")
    public long creationTime;

    transient public ITStateParams params;
    transient public Map<String, String> texts = new ConcurrentHashMap<>();

    public ITState() {
        // Needed by Gson
    }

    protected ITState(Parcel in) {
        domain = in.readString();
        lastChange = in.readLong();
        lastUpdate = in.readLong();
        observerId = in.readString();
        String paramsClass = in.readString();
        if (!TextUtils.isEmpty(paramsClass)) {
            try {
                params = in.readParcelable(Class.forName(paramsClass).getClassLoader());
            } catch (ClassNotFoundException ignored) {
                // Should not happen. If it happens, then let params be null.
            }
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
        validityExpirationDate = in.readLong();
        creationTime = in.readLong();
    }

    /**
     * Returns true if this state is still valid, i.e. its expiry date is in the future (or equals 0, meaning it's not set). Otherwise returns false.
     */
    public boolean isValid() {
        return validityExpirationDate == 0 || validityExpirationDate > System.currentTimeMillis();
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
        if (params == null) {
            dest.writeString("");
        } else {
            dest.writeString(params.getClass().getCanonicalName());
            dest.writeParcelable((Parcelable) params, flags);
        }
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
        dest.writeLong(validityExpirationDate);
        dest.writeLong(creationTime);
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
