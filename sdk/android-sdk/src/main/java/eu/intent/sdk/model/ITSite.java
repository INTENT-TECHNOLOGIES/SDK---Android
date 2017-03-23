package eu.intent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A physical site (building, residence, house).
 */
public class ITSite implements Parcelable {
    public static final Parcelable.Creator<ITSite> CREATOR = new Parcelable.Creator<ITSite>() {
        public ITSite createFromParcel(Parcel source) {
            return new ITSite(source);
        }

        public ITSite[] newArray(int size) {
            return new ITSite[size];
        }
    };

    public ITAddress address;
    public String externalRef;
    public String id;
    public String label;
    public String owner;

    public ITSite() {
        // Needed by Gson
    }

    protected ITSite(Parcel in) {
        address = in.readParcelable(ITAddress.class.getClassLoader());
        externalRef = in.readString();
        id = in.readString();
        label = in.readString();
        owner = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(address, 0);
        dest.writeString(externalRef);
        dest.writeString(id);
        dest.writeString(label);
        dest.writeString(owner);
    }
}
