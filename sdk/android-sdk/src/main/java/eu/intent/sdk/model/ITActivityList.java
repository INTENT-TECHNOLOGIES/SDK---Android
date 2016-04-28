package eu.intent.sdk.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of ITActivity.
 */
public class ITActivityList {
    public List<ITActivity> activities = new ArrayList<>();
    @SerializedName("total")
    public int totalCount = 0;

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
}
