package eu.intent.sdk.model;

import android.text.TextUtils;

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
    public List<ITTag> tags = new ArrayList<>();

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
