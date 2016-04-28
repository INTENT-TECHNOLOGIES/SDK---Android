package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * A task is an instance of a template, which defines the task title and steps.
 *
 * @see ITTask
 */
public class ITTaskTemplate implements Parcelable {
    public static final Parcelable.Creator<ITTaskTemplate> CREATOR = new Parcelable.Creator<ITTaskTemplate>() {
        public ITTaskTemplate createFromParcel(Parcel source) {
            return new ITTaskTemplate(source);
        }

        public ITTaskTemplate[] newArray(int size) {
            return new ITTaskTemplate[size];
        }
    };

    /**
     * These actions' parameters don't have any value, they are just "skeletons" used to create the ITActions of an ITTask.
     */
    public List<ITAction> actions;
    public List<String> deviceTypes;
    public String id;
    public String label;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITTaskTemplate() {
    }

    protected ITTaskTemplate(Parcel in) {
        actions = in.createTypedArrayList(ITAction.CREATOR);
        deviceTypes = in.createStringArrayList();
        id = in.readString();
        label = in.readString();
        custom = in.readBundle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(actions);
        dest.writeStringList(deviceTypes);
        dest.writeString(id);
        dest.writeString(label);
        dest.writeBundle(custom);
    }
}
