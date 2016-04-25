package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * An activity category contains the IDs of several activities.
 *
 * @see ITActivity
 */
public class ITActivityCategory implements Parcelable {
    public static final Parcelable.Creator<ITActivityCategory> CREATOR = new Parcelable.Creator<ITActivityCategory>() {
        public ITActivityCategory createFromParcel(Parcel source) {
            return new ITActivityCategory(source);
        }

        public ITActivityCategory[] newArray(int size) {
            return new ITActivityCategory[size];
        }
    };

    public String icon;
    public String id;
    public String label;
    public String[] activities;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITActivityCategory() {
    }

    protected ITActivityCategory(Parcel in) {
        icon = in.readString();
        id = in.readString();
        label = in.readString();
        activities = in.createStringArray();
        custom = in.readBundle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeString(id);
        dest.writeString(label);
        dest.writeStringArray(activities);
        dest.writeBundle(custom);
    }

    @Override
    public String toString() {
        return label;
    }
}
