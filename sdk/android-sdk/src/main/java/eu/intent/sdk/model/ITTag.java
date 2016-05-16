package eu.intent.sdk.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * A tag has a key, a label and a short label, each for multiple languages.
 */
public class ITTag {
    private static Service sService;
    private static ITTagList sAllTags;

    @SerializedName("classification")
    public String category;
    public String key;
    public String owner;

    transient public Map<String, String> labels = new HashMap<>();
    transient public Map<String, String> shortLabels = new HashMap<>();

    /**
     * Gets the tags that belong to the given category.
     * You must have called loadAll() first in order to load all the tags.
     */
    public static List<ITTag> getFromCategory(Context context, String category) {
        List<ITTag> tags = new ArrayList<>();
        if (!TextUtils.isEmpty(category)) {
            if (sAllTags == null) {
                sAllTags = ITTagList.load(context);
            }
            for (ITTag tag : sAllTags.tags) {
                if (TextUtils.equals(tag.category, category)) {
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    /**
     * Gets the label of the tag with the given key. If no label was found for the current language, it returns the key itself.
     * You must have called loadAll() first in order to load all the tags.
     */
    public static String getLabel(Context context, String key) {
        if (key == null) return "";
        if (sAllTags == null) {
            sAllTags = ITTagList.load(context);
        }
        ITTag tag = sAllTags.get(key);
        if (tag != null) {
            String lang = Locale.getDefault().getLanguage();
            String label = tag.getLabel(lang);
            if (!TextUtils.isEmpty(label)) return label;
        }
        return key;
    }

    /**
     * Gets the short label of the tag with the given key. If no label was found for the current language, it returns the key itself.
     * You must have called loadAll() first in order to load all the tags.
     */
    public static String getShortLabel(Context context, String key) {
        if (key == null) return "";
        if (sAllTags == null) {
            sAllTags = ITTagList.load(context);
        }
        ITTag tag = sAllTags.get(key);
        if (tag != null) {
            String lang = Locale.getDefault().getLanguage();
            String label = tag.getShortLabel(lang);
            if (!TextUtils.isEmpty(label)) return label;
        }
        return key;
    }

    /**
     * Loads all the existing tags for the authenticated user and saves them in the local storage to be retrieved later.
     */
    public static void loadAll(final Context context) {
        getServiceInstance(context).getAll().enqueue(new ProxyCallback<>(new ITApiCallback<ITTagList>() {
            @Override
            public void onSuccess(ITTagList tagList) {
                tagList.save(context);
            }

            @Override
            public void onFailure(int httpCode, String message) {
                Log.e(ITTag.class.getCanonicalName(), "Could not load tags: " + message);
            }
        }));
    }

    private static Service getServiceInstance(Context context) {
        if (sService == null) {
            sService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
        }
        return sService;
    }

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

    private interface Service {
        @GET("datahub/v1/users/system/classifications/tags")
        Call<ITTagList> getAll();
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
