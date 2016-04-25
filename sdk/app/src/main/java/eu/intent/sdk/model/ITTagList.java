package eu.intent.sdk.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of ITTags.
 */
public class ITTagList {
    private static final String PREF_FILE_NAME = "IntentTags.prefs";
    private static final String PREF_TAGS = "tags";

    public List<ITTag> tags = new ArrayList<>();

    private JsonElement json;

    /**
     * Loads the tag list from local storage.
     */
    static ITTagList load(Context context) {
        String jsonTags = context.getSharedPreferences(PREF_FILE_NAME, 0).getString(PREF_TAGS, "");
        if (TextUtils.isEmpty(jsonTags)) {
            Log.w(ITTagList.class.getCanonicalName(), "Please load the tags first by calling ITTag.loadAll()");
            return new ITTagList();
        } else {
            JsonElement json = new Gson().fromJson(jsonTags, JsonElement.class);
            return new Deserializer().deserialize(json, ITTagList.class, null);
        }
    }

    /**
     * Saves this tag list in local storage (replacing any existing tag list).
     */
    void save(Context context) {
        context.getSharedPreferences(PREF_FILE_NAME, 0).edit().putString(PREF_TAGS, json.toString()).apply();
    }

    /**
     * Gets the tag with the given key. Returns null if no such tag was found.
     */
    public ITTag get(String key) {
        for (ITTag tag : tags) {
            if (TextUtils.equals(tag.key, key)) {
                return tag;
            }
        }
        return null;
    }

    public static class Deserializer implements JsonDeserializer<ITTagList> {
        @Override
        public ITTagList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ITTagList tagList = new ITTagList();
            tagList.json = json;
            Type listType = new TypeToken<List<ITTag>>() {
            }.getType();
            JsonArray categories = json.getAsJsonArray();
            Gson gson = new GsonBuilder().registerTypeAdapter(ITTag.class, new ITTag.Deserializer()).create();
            for (JsonElement category : categories) {
                List<ITTag> tags = gson.fromJson(category.getAsJsonObject().get("tags"), listType);
                tagList.tags.addAll(tags);
            }
            return tagList;
        }
    }
}
