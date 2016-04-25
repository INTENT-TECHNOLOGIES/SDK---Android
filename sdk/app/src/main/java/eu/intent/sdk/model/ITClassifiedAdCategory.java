package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

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
 * A category of ITClassifiedAd with its translations.
 *
 * @see ITClassifiedAd
 */
public class ITClassifiedAdCategory implements Parcelable {
    public static final Parcelable.Creator<ITClassifiedAdCategory> CREATOR = new Parcelable.Creator<ITClassifiedAdCategory>() {
        public ITClassifiedAdCategory createFromParcel(Parcel source) {
            return new ITClassifiedAdCategory(source);
        }

        public ITClassifiedAdCategory[] newArray(int size) {
            return new ITClassifiedAdCategory[size];
        }
    };

    private static Service sService;

    public String key;

    transient public Map<String, String> labels = new HashMap<>();

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     */
    transient public Bundle custom = new Bundle();

    public ITClassifiedAdCategory() {
    }

    protected ITClassifiedAdCategory(Parcel in) {
        key = in.readString();
        Bundle labelsBundle = in.readBundle();
        for (String label : labelsBundle.keySet()) {
            labels.put(label, labelsBundle.getString(label));
        }
        custom = in.readBundle();
    }

    /**
     * Gets the categories of classified ads.
     */
    public static void get(Context context, ITApiCallback<List<ITClassifiedAdCategory>> callback) {
        getServiceInstance(context).get().enqueue(new ProxyCallback<>(callback));
    }

    private static Service getServiceInstance(Context context) {
        if (sService == null) {
            sService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
        }
        return sService;
    }

    /**
     * Returns the label for the default Locale. If no label was found, return the key.
     */
    public String getLabel() {
        String label = labels.get(Locale.getDefault().getLanguage());
        return !TextUtils.isEmpty(label) ? label : key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        Bundle labelsBundle = new Bundle();
        for (Map.Entry<String, String> label : labels.entrySet()) {
            labelsBundle.putString(label.getKey(), label.getValue());
        }
        dest.writeBundle(labelsBundle);
        dest.writeBundle(custom);
    }

    @Override
    public String toString() {
        return getLabel();
    }

    private interface Service {
        @GET("residentservices/v1/classifieds/categories")
        Call<List<ITClassifiedAdCategory>> get();
    }

    public static class Deserializer implements JsonDeserializer<ITClassifiedAdCategory> {
        @Override
        public ITClassifiedAdCategory deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ITClassifiedAdCategory category = new Gson().fromJson(json, typeOfT);
            if (json.getAsJsonObject().has("value")) {
                JsonObject value = json.getAsJsonObject().getAsJsonObject("value");
                for (Map.Entry<String, JsonElement> entry : value.entrySet()) {
                    category.labels.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
            return category;
        }
    }
}
