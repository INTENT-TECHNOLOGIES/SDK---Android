package eu.intent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of ITActivity.
 */
public class ITActivityList implements Parcelable {
    public static final Parcelable.Creator<ITActivityList> CREATOR = new Parcelable.Creator<ITActivityList>() {
        @Override
        public ITActivityList createFromParcel(Parcel source) {
            return new ITActivityList(source);
        }

        @Override
        public ITActivityList[] newArray(int size) {
            return new ITActivityList[size];
        }
    };

    public List<ITActivity> activities = new ArrayList<>();
    @SerializedName("total")
    public int totalCount = 0;

    public ITActivityList() {
        // Needed by Gson
    }

    protected ITActivityList(Parcel in) {
        this.activities = in.createTypedArrayList(ITActivity.CREATOR);
        this.totalCount = in.readInt();
    }

    /**
     * Returns an ITActivity from its ID. Returns null if no such ITActivity was found in this list.
     */
    public ITActivity get(String id) {
        for (ITActivity activity : activities) {
            if (TextUtils.equals(activity.id, id)) {
                return activity;
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.activities);
        dest.writeInt(this.totalCount);
    }
}
