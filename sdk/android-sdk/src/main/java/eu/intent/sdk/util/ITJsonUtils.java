package eu.intent.sdk.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.ParseException;

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

    public static long parseJsonIsoDate(@NonNull JsonObject jsonObject, @NonNull String jsonKey) {
        long timestamp = 0;
        JsonElement jsonDate = jsonObject.get(jsonKey);
        if (jsonDate != null && !jsonDate.isJsonNull() && jsonDate.isJsonPrimitive()) {
            try {
                String firstEventDate = jsonDate.getAsString();
                if (!TextUtils.isEmpty(firstEventDate)) {
                    timestamp = ITDateUtils.parseDateIso8601(firstEventDate).getTime();
                }
            } catch (ClassCastException | ParseException ignored) {
                // timestamp will be 0
            }
        }
        return timestamp;
    }
}
