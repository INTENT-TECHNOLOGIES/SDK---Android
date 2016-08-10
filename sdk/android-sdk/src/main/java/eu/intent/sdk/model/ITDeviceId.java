package eu.intent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * A device family has an ID pattern that can be used to validate or autocomplete a device ID.
 *
 * @see ITDeviceType
 */
public class ITDeviceId implements Parcelable {
    public static final Parcelable.Creator<ITDeviceId> CREATOR = new Parcelable.Creator<ITDeviceId>() {
        public ITDeviceId createFromParcel(Parcel source) {
            return new ITDeviceId(source);
        }

        public ITDeviceId[] newArray(int size) {
            return new ITDeviceId[size];
        }
    };

    @SerializedName("padWith")
    public String placeholderChar = "";
    public String prefix = "";
    public String regex = "";
    @SerializedName("size")
    public int variableSize = 0;

    public ITDeviceId() {
    }

    protected ITDeviceId(Parcel in) {
        placeholderChar = in.readString();
        prefix = in.readString();
        regex = in.readString();
        variableSize = in.readInt();
    }

    /**
     * Gets the brand part of the prefix, ie the text until the last colon (:). For example, "urn:intent:083_" will turn to "urn:intent:".
     */
    public String getBrandPrefix() {
        int indexOfLastColon = prefix.lastIndexOf(":");
        if (indexOfLastColon >= 0 && indexOfLastColon < prefix.length()) {
            return prefix.substring(0, indexOfLastColon + 1);
        }
        return "";
    }

    /**
     * Gets the prefix without the brand name, ie after the last colon (:). For example, "urn:intent:083_" will turn to "083".
     */
    public String getShortPrefix() {
        if (!TextUtils.isEmpty(prefix)) {
            return prefix.replace(getBrandPrefix(), "");
        }
        return "";
    }

    /**
     * Gets a short representation of the given ID.
     */
    public String getShortId(String id) {
        if (!TextUtils.isEmpty(id)) {
            return id.replaceFirst(getBrandPrefix(), "");
        } else {
            return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeholderChar);
        dest.writeString(prefix);
        dest.writeString(regex);
        dest.writeInt(variableSize);
    }
}
