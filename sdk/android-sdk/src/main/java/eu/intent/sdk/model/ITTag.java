package eu.intent.sdk.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A tag has a key, a label and a short label, each for multiple languages.
 */
public class ITTag {
    @SerializedName("classification")
    public String category;
    public String key;
    public String owner;

    transient public Map<String, String> labels = new ConcurrentHashMap<>();
    transient public Map<String, String> shortLabels = new ConcurrentHashMap<>();

    /**
     * Returns the tag label in the given language. Returns the tag key if no label was found.
     *
     * @param lang the language code ("fr", "en",...)
     */
    public String getLabel(String lang) {
        if (lang == null) return key;
        String label = labels.get(lang);
        return TextUtils.isEmpty(label) ? key : label;
    }

    /**
     * Returns the tag short label in the given language. Returns the tag key if no short label was found.
     *
     * @param lang the language code ("fr", "en",...)
     */
    public String getShortLabel(String lang) {
        String label = shortLabels.get(lang);
        return TextUtils.isEmpty(label) ? key : label;
    }

    public static class Deserializer implements JsonDeserializer<ITTag> {
        @Override
        public ITTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new Gson();
            ITTag tag = gson.fromJson(json, typeOfT);
            Type mapType = new TypeToken<Map<String, String>>() {
            }.getType();
            JsonObject labels = json.getAsJsonObject().getAsJsonObject("label");
            JsonObject shortLabels = json.getAsJsonObject().getAsJsonObject("short_label");
            tag.labels = gson.fromJson(labels, mapType);
            tag.shortLabels = gson.fromJson(shortLabels, mapType);
            return tag;
        }
    }
}
