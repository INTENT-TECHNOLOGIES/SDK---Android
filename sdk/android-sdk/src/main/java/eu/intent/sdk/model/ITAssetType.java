package eu.intent.sdk.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * An asset can be a site, a part, or an equipment.
 */
public enum ITAssetType {
    @SerializedName("equip")EQUIPMENT,
    @SerializedName("part")PART,
    @SerializedName("site")SITE,
    UNKNOWN;

    private static final String EQUIPMENT_LABEL = "equip";

    public static ITAssetType fromString(String name) {
        try {
            return ITAssetType.valueOf(name.toUpperCase(Locale.US));
        } catch (IllegalArgumentException e) {
            if (TextUtils.equals(name, EQUIPMENT_LABEL)) {
                return EQUIPMENT;
            }
            return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        String s;
        if (this == EQUIPMENT) {
            s = EQUIPMENT_LABEL;
        } else {
            s = name().toLowerCase();
        }
        return s;
    }
}
