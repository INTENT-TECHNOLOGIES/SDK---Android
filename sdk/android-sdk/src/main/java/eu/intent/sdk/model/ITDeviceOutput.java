package eu.intent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A device can send data for each one of its outputs. Each output can represent an activity among a given list of activities. A "required" output will be computed by the platform and must have an associated activity. Optional outputs only have associated activities if they are used, (i.e. they are plugged to a piece of equipment). A device can require parameters in order to compute its data.
 *
 * @see ITDevice
 */
public class ITDeviceOutput implements Parcelable {
    public static final Parcelable.Creator<ITDeviceOutput> CREATOR = new Parcelable.Creator<ITDeviceOutput>() {
        public ITDeviceOutput createFromParcel(Parcel source) {
            return new ITDeviceOutput(source);
        }

        public ITDeviceOutput[] newArray(int size) {
            return new ITDeviceOutput[size];
        }
    };

    @SerializedName("availableActivities")
    public List<String> activities;
    public String key;
    public List<Param> params;
    @SerializedName("requireActivity")
    public boolean required;

    public ITDeviceOutput() {
    }

    protected ITDeviceOutput(Parcel in) {
        activities = in.createStringArrayList();
        key = in.readString();
        params = in.createTypedArrayList(Param.CREATOR);
        required = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(activities);
        dest.writeString(key);
        dest.writeTypedList(params);
        dest.writeByte(required ? (byte) 1 : (byte) 0);
    }

    /**
     * A device output can need parameters in order for the platform to compute the received data.
     */
    public static class Param implements Parcelable {
        public static final Parcelable.Creator<Param> CREATOR = new Parcelable.Creator<Param>() {
            public Param createFromParcel(Parcel source) {
                return new Param(source);
            }

            public Param[] newArray(int size) {
                return new Param[size];
            }
        };

        public String key;
        public String pattern;

        public Param() {
        }

        protected Param(Parcel in) {
            key = in.readString();
            pattern = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(key);
            dest.writeString(pattern);
        }
    }
}
