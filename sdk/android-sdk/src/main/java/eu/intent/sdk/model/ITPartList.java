package eu.intent.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of ITParts.
 */
public class ITPartList {
    public List<ITPart> parts = new ArrayList<>();
    @SerializedName("total")
    public int totalCount = 0;
}
