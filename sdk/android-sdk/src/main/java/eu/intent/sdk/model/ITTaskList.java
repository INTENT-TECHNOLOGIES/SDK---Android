package eu.intent.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of ITTask.
 */
public class ITTaskList {
    public List<ITTask> tasks = new ArrayList<>();
    @SerializedName("count")
    public int totalCount = 0;
}
