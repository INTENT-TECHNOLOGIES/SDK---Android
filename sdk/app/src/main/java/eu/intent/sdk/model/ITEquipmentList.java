package eu.intent.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of ITEquipments.
 */
public class ITEquipmentList {
    public List<ITEquipment> equipments = new ArrayList<>();
    @SerializedName("total")
    public int totalCount = 0;
}
