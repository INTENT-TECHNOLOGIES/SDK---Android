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
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * A green gesture is an advice to an occupant to help him reduce his energy bill and be environmentally friendly.
 */
public class ITGreenGesture implements Parcelable {
    public static final Parcelable.Creator<ITGreenGesture> CREATOR = new Parcelable.Creator<ITGreenGesture>() {
        public ITGreenGesture createFromParcel(Parcel source) {
            return new ITGreenGesture(source);
        }

        public ITGreenGesture[] newArray(int size) {
            return new ITGreenGesture[size];
        }
    };

    private static Service sService;

    /**
     * Duration in weeks
     */
    @SerializedName("period")
    public int duration;
    /**
     * True if this green gesture can be shown to the users, false otherwise
     */
    @SerializedName("activated")
    public boolean enabled;
    @SerializedName("uuid")
    public String id;
    /**
     * Percentage of estimated consumption savings
     */
    @SerializedName("goal")
    public int savings;
    @SerializedName("description")
    public String text;
    public String title;

    transient  public ITEnergy energy;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITGreenGesture() {
    }

    protected ITGreenGesture(Parcel in) {
        duration = in.readInt();
        enabled = in.readByte() != 0;
        id = in.readString();
        savings = in.readInt();
        text = in.readString();
        title = in.readString();
        int tmpEnergy = in.readInt();
        energy = tmpEnergy == -1 ? null : ITEnergy.values()[tmpEnergy];
        custom = in.readBundle();
    }

    /**
     * Gets the green gestures proposed to the authenticated user.
     */
    public static void get(Context context, ITApiCallback<ITGreenGestureList> callback) {
        get(context, null, callback);
    }

    /**
     * Gets the green gestures proposed to the authenticated user.
     *
     * @param energies an array of ITEnergy
     */
    public static void get(Context context, ITEnergy[] energies, ITApiCallback<ITGreenGestureList> callback) {
        String typesString = TextUtils.join(",", energies != null && energies.length > 0 ? energies : ITEnergy.values());
        getServiceInstance(context).get(typesString, 1, Integer.MAX_VALUE).enqueue(new ProxyCallback<>(callback));
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
        dest.writeInt(duration);
        dest.writeByte(enabled ? (byte) 1 : (byte) 0);
        dest.writeString(id);
        dest.writeInt(savings);
        dest.writeString(text);
        dest.writeString(title);
        dest.writeInt(energy == null ? -1 : energy.ordinal());
        dest.writeBundle(custom);
    }

    private interface Service {
        @GET("v1/green-gestures")
        Call<ITGreenGestureList> get(@Query("energy") String energies, @Query("page") int page, @Query("countByPage") int count);
    }

    public static class Deserializer implements JsonDeserializer<ITGreenGesture> {
        @Override
        public ITGreenGesture deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ITGreenGesture gesture = new Gson().fromJson(json, typeOfT);
            try {
                gesture.energy = ITEnergy.valueOf(json.getAsJsonObject().get("energy").getAsString().toUpperCase(Locale.US));
            } catch (IllegalArgumentException e) {
                gesture.energy = ITEnergy.UNKNOWN;
            }
            return gesture;
        }
    }
}
