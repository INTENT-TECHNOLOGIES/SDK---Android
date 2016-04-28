package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * An Intent address.
 */
public class ITAddress implements Parcelable {
    public static final Creator<ITAddress> CREATOR = new Creator<ITAddress>() {
        public ITAddress createFromParcel(Parcel source) {
            return new ITAddress(source);
        }

        public ITAddress[] newArray(int size) {
            return new ITAddress[size];
        }
    };
    public String city;
    public String complement;
    public String country;
    @SerializedName("pos")
    public ITLocation location = new ITLocation();
    public String num;
    public String way;
    public String zip;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITAddress() {
    }

    protected ITAddress(Parcel in) {
        city = in.readString();
        complement = in.readString();
        country = in.readString();
        location = in.readParcelable(ITLocation.class.getClassLoader());
        num = in.readString();
        way = in.readString();
        zip = in.readString();
        custom = in.readBundle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(complement);
        dest.writeString(country);
        dest.writeParcelable(location, flags);
        dest.writeString(num);
        dest.writeString(way);
        dest.writeString(zip);
        dest.writeBundle(custom);
    }
}
