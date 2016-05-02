package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * A reply to an ITClassifiedAd.
 *
 * @see ITClassifiedAd
 */
public class ITClassifiedAdReply implements Parcelable {
    public static final Parcelable.Creator<ITClassifiedAdReply> CREATOR = new Parcelable.Creator<ITClassifiedAdReply>() {
        public ITClassifiedAdReply createFromParcel(Parcel source) {
            return new ITClassifiedAdReply(source);
        }

        public ITClassifiedAdReply[] newArray(int size) {
            return new ITClassifiedAdReply[size];
        }
    };

    @SerializedName("responseDate")
    public long date;
    @SerializedName("mail")
    public String contactEmail;
    @SerializedName("userId")
    public String contactId;
    @SerializedName("name")
    public String contactName;
    @SerializedName("phone")
    public String contactPhone;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITClassifiedAdReply() {
    }

    protected ITClassifiedAdReply(Parcel in) {
        date = in.readLong();
        contactEmail = in.readString();
        contactId = in.readString();
        contactName = in.readString();
        contactPhone = in.readString();
        custom = in.readBundle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date);
        dest.writeString(contactEmail);
        dest.writeString(contactId);
        dest.writeString(contactName);
        dest.writeString(contactPhone);
        dest.writeBundle(custom);
    }
}
