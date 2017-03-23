package eu.intent.sdk.model;

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

    public ITClassifiedAdReply() {
        // Needed by Gson
    }

    protected ITClassifiedAdReply(Parcel in) {
        date = in.readLong();
        contactEmail = in.readString();
        contactId = in.readString();
        contactName = in.readString();
        contactPhone = in.readString();
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
    }
}
