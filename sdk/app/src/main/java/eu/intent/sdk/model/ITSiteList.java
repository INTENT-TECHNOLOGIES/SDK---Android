package eu.intent.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of ITSite.
 */
public class ITSiteList {
    public List<ITSite> sites = new ArrayList<>();
    @SerializedName("total")
    public int totalCount = 0;
}
