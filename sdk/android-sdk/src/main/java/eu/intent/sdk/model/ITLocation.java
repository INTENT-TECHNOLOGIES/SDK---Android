package eu.intent.sdk.model;

import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Coordinates, with a latitude and a longitude.
 */
public class ITLocation implements Parcelable {
    public static final Parcelable.Creator<ITLocation> CREATOR = new Parcelable.Creator<ITLocation>() {
        public ITLocation createFromParcel(Parcel source) {
            return new ITLocation(source);
        }

        public ITLocation[] newArray(int size) {
            return new ITLocation[size];
        }
    };

    transient public double lat;
    transient public double lng;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITLocation() {
        // Needed by Retrofit
    }

    public ITLocation(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public ITLocation(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
    }

    protected ITLocation(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
        custom = in.readBundle();
    }

    /**
     * Returns the distance in meter between this location and the given location.
     *
     * @see Location#distanceBetween(double, double, double, double, float[])
     */
    public float distanceFrom(ITLocation other) {
        float[] results = new float[1];
        Location.distanceBetween(lat, lng, other.lat, other.lng, results);
        return results[0];
    }

    /**
     * Returns true if the coordinates are valid (i.e. not (0,0)).
     */
    public boolean isValid() {
        return !Double.isNaN(lat) && !Double.isNaN(lng) && (lat != 0 || lng != 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeBundle(custom);
    }

    @Override
    public String toString() {
        return "(" + lat + ", " + lng + ")";
    }

    public static class Deserializer implements JsonDeserializer<ITLocation> {
        @Override
        public ITLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new Gson();
            JsonObject jsonObject = json.getAsJsonObject();
            ITLocation location = gson.fromJson(json, typeOfT);
            if (jsonObject.has("lat")) {
                location.lat = jsonObject.get("lat").getAsDouble();
            }
            if (jsonObject.has("lng")) {
                location.lng = jsonObject.get("lng").getAsDouble();
            }
            if (jsonObject.has("coordinates")) {
                JsonArray coordinates = jsonObject.get("coordinates").getAsJsonArray();
                location.lng = coordinates.get(0).getAsDouble();
                location.lat = coordinates.get(1).getAsDouble();
            }
            return location;
        }
    }
}
