package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the "family" of a device, defining its vendor and its properties, including the device outputs.
 *
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
    public List<ScanTag> scanTags = new ArrayList<>();
    @SerializedName("plugged")
    public boolean wired;

    transient public ITDeviceId idPattern = new ITDeviceId();
    transient public Map<String, String> labels = new ConcurrentHashMap<>();
    transient public String number;
    transient public String vendor;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITDeviceType() {
        // Needed by Retrofit
    }

    protected ITDeviceType(Parcel in) {
        in.readStringList(functions);
        fullName = in.readString();
        name = in.readString();
        outputs = in.createTypedArrayList(ITDeviceOutput.CREATOR);
        scanTags = in.createTypedArrayList(ScanTag.CREATOR);
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

    /**
     * @return a ScanTag of the given type if this device type is equipped, null otherwise.
     * If there are several ScanTags matching the given type, one of them is returned randomly.
     */
    @Nullable
    public ScanTag getScanTag(String tagType) {
        ScanTag result = null;
        if (scanTags != null) {
            for (ScanTag scanTag : scanTags) {
                if (TextUtils.equals(scanTag.type, tagType)) {
                    result = scanTag;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @return true if this device type is equipped with the given tag type, false otherwise
     */
    public boolean hasScanTag(String tagType) {
        return getScanTag(tagType) != null;
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
        dest.writeTypedList(scanTags);
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
                JsonElement productNumber = vendor.get("productNumber");
                JsonElement alias = vendor.get("alias");
                deviceType.number = productNumber == null || productNumber.isJsonNull() ? "" : productNumber.getAsString();
                deviceType.vendor = alias == null || alias.isJsonNull() ? "" : alias.getAsString();
                if (vendor.has("idFormat")) {
                    deviceType.idPattern = gson.fromJson(vendor.get("idFormat"), ITDeviceId.class);
                }
            }
            return deviceType;
        }
    }

    /**
     * A device can be equipped with NFC, QRcode, barcode,...
     */
    public static class ScanTag implements Parcelable {
        /**
         * The tag is a QRcode.
         */
        public static final String TYPE_QRCODE = "qrcode";
        /**
         * The tag is NFC.
         */
        public static final String TYPE_NFC = "nfc";

        public static final Creator<ScanTag> CREATOR = new Creator<ScanTag>() {
            @Override
            public ScanTag createFromParcel(Parcel source) {
                return new ScanTag(source);
            }

            @Override
            public ScanTag[] newArray(int size) {
                return new ScanTag[size];
            }
        };

        @Nullable
        public Decoder decoder;
        @Nullable
        public String type;

        public ScanTag() {
            // Needed by Retrofit
        }

        protected ScanTag(Parcel in) {
            decoder = in.readParcelable(Decoder.class.getClassLoader());
            type = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(decoder, flags);
            dest.writeString(type);
        }

        /**
         * The method used to decode a tag. If no type is provided, the tag contains the value to use.
         * The data property contains any additional information that can be util to decode the tag.
         */
        public static class Decoder implements Parcelable {
            /**
             * The value is encoded with a custom algorithm.
             * Call getDecodeUrl() to get the complete URL to decode the tag content.
             */
            public static final String TYPE_URL = "url";

            public static final Creator<Decoder> CREATOR = new Creator<Decoder>() {
                @Override
                public Decoder createFromParcel(Parcel source) {
                    return new Decoder(source);
                }

                @Override
                public Decoder[] newArray(int size) {
                    return new Decoder[size];
                }
            };

            private static final String URL_TAG_CONTENT_PLACEHOLDER = "{content}";

            @Nullable
            public String data;
            @Nullable
            public String type;

            public Decoder() {
                // Needed by Retrofit
            }

            protected Decoder(Parcel in) {
                data = in.readString();
                type = in.readString();
            }

            /**
             * Appends the given tag content to the decoder URL, only if the decoder type is TYPE_URL.
             * Otherwise return an empty string.
             *
             * @param tagContent the raw content of the tag
             * @return the full URL to call to decode the tag
             */
            @NonNull
            public String getDecodeUrl(String tagContent) {
                String url = "";
                if (TextUtils.equals(type, TYPE_URL) && !TextUtils.isEmpty(data)) {
                    if (TextUtils.isEmpty(tagContent)) {
                        url = data;
                    } else {
                        url = data.replace(URL_TAG_CONTENT_PLACEHOLDER, tagContent);
                    }
                }
                return url;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(data);
                dest.writeString(type);
            }
        }
    }
}
