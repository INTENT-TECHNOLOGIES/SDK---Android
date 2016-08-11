package eu.intent.sdk.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.JsonObject;

/**
 * Provides some util methods to work with JSON.
 */
public final class ITJsonUtils {
    private ITJsonUtils() {
    }

    @Nullable
    public static String getJsonAsString(@NonNull JsonObject json, @NonNull String key, String defaultValue) {
        String result = defaultValue;
        if (json.has(key) && !json.get(key).isJsonNull()) {
            result = json.get(key).getAsString();
        }
        return result;
    }
}
