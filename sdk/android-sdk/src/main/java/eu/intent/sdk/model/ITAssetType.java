package eu.intent.sdk.model;

import android.text.TextUtils;

import java.util.Locale;

/**
 * An asset can be a site, a part, or an equipment.
 */
public enum ITAssetType {
    EQUIPMENT, PART, SITE, UNKNOWN;

    public static ITAssetType fromString(String name) {
        try {
            return ITAssetType.valueOf(name.toUpperCase(Locale.US));
        } catch (IllegalArgumentException e) {
            if (TextUtils.equals(name, "equip")) {
                return EQUIPMENT;
            }
            return UNKNOWN;
        }
    }
}
