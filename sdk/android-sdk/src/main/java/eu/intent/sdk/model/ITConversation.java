package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A conversation linked to a site and {@link ITActivityCategory}
 */
public class ITConversation implements Parcelable {
    public static final Parcelable.Creator<ITConversation> CREATOR = new Parcelable.Creator<ITConversation>() {
        public ITConversation createFromParcel(Parcel source) {
            return new ITConversation(source);
        }

        public ITConversation[] newArray(int size) {
            return new ITConversation[size];
        }
    };

    private static Service sService;

    public String assetId;
    public ITAssetType assetType;
    public String activityCategory;
    public List<ITMessage> messages;
    public int totalMessagesCount;
    public long lastUpdate;
    public long creationDate;
    public long lastRead;
    public String creatorDomain;
    public String creatorName;
    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    public Bundle custom = new Bundle();

    public ITConversation() {
        // Needed by Retrofit
    }

    protected ITConversation(Parcel in) {
        assetId = in.readString();
        int tmpAssetType = in.readInt();
        assetType = tmpAssetType == -1 ? null : ITAssetType.values()[tmpAssetType];
        activityCategory = in.readString();
        messages = new ArrayList<>();
        in.readList(this.messages, List.class.getClassLoader());
        totalMessagesCount = in.readInt();
        lastUpdate = in.readLong();
        creationDate = in.readLong();
        lastRead = in.readLong();
        creatorDomain = in.readString();
        creatorName = in.readString();
        custom = in.readBundle();
    }

    /**
     * Get conversation
     *
     * @param siteId             the ITSite's Intent id
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     */
    public static void getBySiteAndActivityCategory(Context context, String siteId, String activityCategoryId, ITApiCallback<ITConversation> callback) {
        getBySiteAndActivityCategory(context, siteId, activityCategoryId, 0, callback);
    }

    /**
     * Mark a conversation as read by siteId and activityCategory
     *
     * @param messageId Id of the last read message
     */
    public static void markAsRead(Context context, String messageId, ITApiCallback<Void> callback) {
        getServiceInstance(context).markAsRead(messageId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Get conversation with message since specified date
     *
     * @param siteId             the ITSite's Intent id
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param updatedSince       Getting messages from this timestamp
     */
    public static void getBySiteAndActivityCategory(Context context, String siteId, String activityCategoryId, long updatedSince, ITApiCallback<ITConversation> callback) {
        getServiceInstance(context).get(activityCategoryId, "site", siteId, updatedSince).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Get conversation with message by page
     *
     * @param siteId             the ITSite's Intent id
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param page               Messages page (start at 1)
     * @param countByPage        Messages count by page
     */
    public static void getBySiteAndActivityCategoryPaginated(Context context, String siteId, String activityCategoryId, int page, int countByPage, ITApiCallback<ITConversation> callback) {
        getServiceInstance(context).get(activityCategoryId, "site", siteId, page, countByPage).enqueue(new ProxyCallback<>(callback));
    }

    private static Service getServiceInstance(Context context) {
        synchronized (ITConversation.class) {
            if (sService == null) {
                sService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
            }
        }
        return sService;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(assetId);
        dest.writeInt(assetType == null ? -1 : assetType.ordinal());
        dest.writeString(this.activityCategory);
        dest.writeList(this.messages);
        dest.writeInt(this.totalMessagesCount);
        dest.writeLong(this.lastUpdate);
        dest.writeLong(this.creationDate);
        dest.writeLong(this.lastRead);
        dest.writeString(this.creatorDomain);
        dest.writeString(this.creatorName);
        dest.writeBundle(custom);
    }

    private interface Service {
        @GET("reports/v1/")
        Call<ITConversation> get(@Query("theme") String theme, @Query("assetType") String assetType, @Query("assetId") String assetId, @Query("since") long updatedSince);

        @GET("reports/v1/")
        Call<ITConversation> get(@Query("theme") String theme, @Query("assetType") String assetType, @Query("assetId") String assetId, @Query("page") long page, @Query("countByPage") long countByPage);

        @POST("reports/v1/markAsRead/{messageId}")
        Call<Void> markAsRead(@Path("messageId") String theme);
    }

    public static class Deserializer implements JsonDeserializer<ITConversation> {
        @Override
        public ITConversation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder().registerTypeAdapter(ITMessage.class, new ITMessage.Deserializer()).create();
            ITConversation conversation = gson.fromJson(json, typeOfT);
            JsonObject link = json.getAsJsonObject().getAsJsonObject("link");
            conversation.activityCategory = link.get("theme").getAsString();
            conversation.assetId = link.get("assetId").getAsString();
            conversation.assetType = ITAssetType.fromString(link.get("assetType").getAsString().toUpperCase(Locale.US));
            return conversation;
        }
    }
}
