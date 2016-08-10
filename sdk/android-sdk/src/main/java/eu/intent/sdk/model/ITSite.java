package eu.intent.sdk.model;

import android.os.Bundle;
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

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITSite() {
    }

    protected ITSite(Parcel in) {
        address = in.readParcelable(ITAddress.class.getClassLoader());
        externalRef = in.readString();
        id = in.readString();
        label = in.readString();
        owner = in.readString();
        custom = in.readBundle();
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
        dest.writeBundle(custom);
    }
}
