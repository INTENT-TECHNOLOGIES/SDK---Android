package eu.intent.sdk.util;

import android.support.annotation.NonNull;

import com.google.gson.JsonObject;

/**
 * Provides some util methods to work with JSON.
 */
public final class ITJsonUtils {
    private ITJsonUtils() {
    }

    @NonNull
    public static String getJsonAsString(@NonNull JsonObject json, @NonNull String key, @NonNull String defaultValue) {
        String result = defaultValue;
        if (json.has(key) && !json.get(key).isJsonNull()) {
            result = json.get(key).getAsString();
        }
        return result;
    }
}
