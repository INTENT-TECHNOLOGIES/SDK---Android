package eu.intent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A part of a site, either common (eg boiler installRoom) or private (an habitation).
 */
public class ITPart implements Parcelable {
    public static final Parcelable.Creator<ITPart> CREATOR = new Parcelable.Creator<ITPart>() {
        public ITPart createFromParcel(Parcel source) {
            return new ITPart(source);
        }

        public ITPart[] newArray(int size) {
            return new ITPart[size];
        }
    };

    public ITAddress address;
    public String door;
    public String externalRef;
    public String id;
    @SerializedName("level")
    public String floor;
    public String label;
    public String owner;
    public ITPortion portion;
    public String siteId;
    public List<ITUser> users = new ArrayList<>();

    public ITPart() {
        // Needed by Gson
    }

    protected ITPart(Parcel in) {
        address = in.readParcelable(ITAddress.class.getClassLoader());
        door = in.readString();
        externalRef = in.readString();
        id = in.readString();
        floor = in.readString();
        label = in.readString();
        owner = in.readString();
        int tmpPortion = in.readInt();
        portion = tmpPortion == -1 ? null : ITPortion.values()[tmpPortion];
        siteId = in.readString();
        users = new ArrayList<>();
        in.readList(users, ITUser.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(address, 0);
        dest.writeString(door);
        dest.writeString(externalRef);
        dest.writeString(id);
        dest.writeString(floor);
        dest.writeString(label);
        dest.writeString(owner);
        dest.writeInt(portion == null ? -1 : portion.ordinal());
        dest.writeString(siteId);
        dest.writeList(users);
    }

    public enum ITPortion {
        @SerializedName("commonPortion")COMMON,
        @SerializedName("privatePortion")PRIVATE,
        UNKNOWN
    }
}
