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
 * This class represents a piece of data, at a given date, with a given value. In a few cases there may be a valueMin and valueMax instead of value.
 */
public class ITData implements Parcelable {
    public static final Parcelable.Creator<ITData> CREATOR = new Parcelable.Creator<ITData>() {
        public ITData createFromParcel(Parcel source) {
            return new ITData(source);
        }

        public ITData[] newArray(int size) {
            return new ITData[size];
        }
    };

    public long timestamp;
    @SerializedName("trustlevel")
    public TrustLevel trustLevel;

    transient public double value = Double.NaN;
    transient public double valueMin = Double.NaN;
    transient public double valueMax = Double.NaN;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITData() {
    }

    protected ITData(Parcel in) {
        timestamp = in.readLong();
        value = in.readDouble();
        int tmpTrustLevel = in.readInt();
        trustLevel = tmpTrustLevel == -1 ? null : TrustLevel.values()[tmpTrustLevel];
        custom = in.readBundle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(timestamp);
        dest.writeDouble(value);
        dest.writeInt(trustLevel == null ? -1 : trustLevel.ordinal());
        dest.writeBundle(custom);
    }

    public enum Granularity {
        DAY, HOUR, MANUAL
    }

    public enum Type {
        /**
         * Relative to something that is not normal
         */
        ALERT,
        /**
         * The average of several values on a period of time
         */
        AVERAGE,
        /**
         * The difference between two values on a period of time
         */
        DELTA,
        /**
         * A snapshot of the data at a given time
         */
        SNAPSHOT,
        /**
         * A technical intervention
         */
        TICKET,
        UNKNOWN;

        @Override
        public String toString() {
            return name().toLowerCase(Locale.US);
        }
    }

    public enum TrustLevel {
        @SerializedName("COMPUTED")COMPUTED,
        @SerializedName("ESTIMATE")ESTIMATE,
        @SerializedName("MANUAL")MANUAL,
        @SerializedName("SENSOR")SENSOR,
        @SerializedName("TRUSTED_MANUAL")TRUSTED_MANUAL,
        @SerializedName("TRUSTED_SENSOR")TRUSTED_SENSOR,
        UNKNOWN
    }

    public static class Deserializer implements JsonDeserializer<ITData> {
        @Override
        public ITData deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new Gson();
            ITData data = gson.fromJson(json, typeOfT);
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement jsonValue = jsonObject.get("value");
            if (jsonValue.isJsonObject()) {
                JsonObject value = jsonObject.getAsJsonObject("value");
                data.valueMin = value.get("min").getAsDouble();
                data.valueMax = value.get("max").getAsDouble();
            } else if (jsonValue.isJsonPrimitive()) {
                data.value = jsonValue.getAsDouble();
            }
            return data;
        }
    }
}
