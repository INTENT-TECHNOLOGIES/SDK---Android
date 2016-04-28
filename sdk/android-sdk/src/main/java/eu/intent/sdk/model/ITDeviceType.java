package eu.intent.sdk.model;

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
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This is the "family" of a device, defining its vendor and its properties, including the device outputs.
 *
 * @see ITDevice
 * @see ITDeviceId
 */
public class ITDeviceType implements Parcelable {
    public static final Parcelable.Creator<ITDeviceType> CREATOR = new Parcelable.Creator<ITDeviceType>() {
        public ITDeviceType createFromParcel(Parcel source) {
            return new ITDeviceType(source);
        }

        public ITDeviceType[] newArray(int size) {
            return new ITDeviceType[size];
        }
    };

    public List<String> functions = new ArrayList<>();
    @SerializedName("fullLabel")
    public String fullName;
    @SerializedName("label")
    public String name;
    public List<ITDeviceOutput> outputs = new ArrayList<>();
    @SerializedName("plugged")
    public boolean wired;

    transient public ITDeviceId idPattern = new ITDeviceId();
    transient public Map<String, String> labels = new HashMap<>();
    transient public String number;
    transient public String vendor;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITDeviceType() {
    }

    protected ITDeviceType(Parcel in) {
        in.readStringList(functions);
        fullName = in.readString();
        name = in.readString();
        outputs = in.createTypedArrayList(ITDeviceOutput.CREATOR);
        wired = in.readByte() > 0;
        idPattern = in.readParcelable(ITDeviceId.class.getClassLoader());
        Bundle labelsBundle = in.readBundle();
        for (String label : labelsBundle.keySet()) {
            labels.put(label, labelsBundle.getString(label));
        }
        number = in.readString();
        vendor = in.readString();
        custom = in.readBundle();
    }

    /**
     * Returns the outputs of this device type, that can measure data for the given activity.
     */
    public List<ITDeviceOutput> getOutputsForActivity(String activity) {
        List<ITDeviceOutput> outputs = new ArrayList<>();
        for (ITDeviceOutput output : this.outputs) {
            if (output.activities.contains(activity)) {
                outputs.add(output);
            }
        }
        return outputs;
    }

    public String getLabel() {
        String label = labels.get(Locale.getDefault().getLanguage());
        if (!TextUtils.isEmpty(label)) {
            return label;
        } else {
            return fullName;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(functions);
        dest.writeString(fullName);
        dest.writeString(name);
        dest.writeTypedList(outputs);
        dest.writeByte((byte) (wired ? 1 : 0));
        dest.writeParcelable(idPattern, flags);
        Bundle labelsBundle = new Bundle();
        for (Map.Entry<String, String> label : labels.entrySet()) {
            labelsBundle.putString(label.getKey(), label.getValue());
        }
        dest.writeBundle(labelsBundle);
        dest.writeString(number);
        dest.writeString(vendor);
        dest.writeBundle(custom);
    }

    public static class Deserializer implements JsonDeserializer<ITDeviceType> {
        @Override
        public ITDeviceType deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new Gson();
            ITDeviceType deviceType = gson.fromJson(json, typeOfT);
            JsonObject jsonObject = json.getAsJsonObject();
            JsonObject labels = jsonObject.getAsJsonObject("name");
            deviceType.labels = gson.fromJson(labels, new TypeToken<Map<String, String>>() {
            }.getType());
            if (jsonObject.has("vendor")) {
                JsonObject vendor = jsonObject.getAsJsonObject("vendor");
                deviceType.number = vendor.get("productNumber").isJsonNull() ? "" : vendor.get("productNumber").getAsString();
                deviceType.vendor = vendor.get("alias").getAsString();
                if (vendor.has("idFormat")) {
                    deviceType.idPattern = gson.fromJson(vendor.get("idFormat"), ITDeviceId.class);
                }
            }
            return deviceType;
        }
    }
}
