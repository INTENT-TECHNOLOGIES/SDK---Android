package eu.intent.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of ITOperation.
 */
public class ITOperationList {
    public List<ITOperation> operations = new ArrayList<>();
    @SerializedName("count")
    public int totalCount = 0;
}
