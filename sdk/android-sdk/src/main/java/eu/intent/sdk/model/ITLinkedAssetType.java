package eu.intent.sdk.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Several APIs allow to fetch items for an assets, including its "linked assets".
 * For example, you may want to fetch the streams of a site and its parts.
 */

public enum ITLinkedAssetType {
    @SerializedName("all")ALL,
    @SerializedName("equips")EQUIPMENTS,
    @SerializedName("partsEquips")PART_EQUIPMENTS,
    @SerializedName("parts")PARTS,
    @SerializedName("sites")SITES;

    @NonNull
    @Override
    public String toString() {
        try {
            return getDeclaringClass().getDeclaredField(name()).getAnnotation(SerializedName.class).value();
        } catch (NoSuchFieldException e) {
            return "";
        }
    }
}
