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
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Message that take part of a {@link ITConversation}
 * /!\ some class members can be null depending of the ITMessage {@link eu.intent.sdk.model.ITMessage.MessageType}
 * <p/>
 * reportingState, reportingStateLastChange and reportingStateHistory are only set if type is ITMessage.MessageType.REPORTING, </br>
 * state is only set if type is ITMessage.MessageType.STATE
 */
public class ITMessage implements Parcelable {
    public static final Parcelable.Creator<ITMessage> CREATOR = new Parcelable.Creator<ITMessage>() {
        public ITMessage createFromParcel(Parcel source) {
            return new ITMessage(source);
        }

        public ITMessage[] newArray(int size) {
            return new ITMessage[size];
        }
    };
    private static Service sService;
    public MessageType type;
    public String id;
    public String title;
    public String message;
    public boolean removed;
    public long creationDate;
    public long lastUpdate;
    public String creatorName;
    public String creatorDomain;

    //Reporting type only
    /**
     * Current reporting state, only present if type is REPORTING
     */
    public ReportingState reportingState;
    /**
     * History of reporting state, contains the current state and only present if type is REPORTING
     */
    public List<ReportingStateHistoryItem> reportingStateHistory;

    //State type only
    /**
     * State payload of the message, only present if type is STATE
     */
    public ITState state;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    public Bundle custom = new Bundle();

    public ITMessage() {
    }

    protected ITMessage(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : MessageType.values()[tmpType];
        this.id = in.readString();
        this.title = in.readString();
        this.message = in.readString();
        this.removed = in.readByte() != 0;
        this.creationDate = in.readLong();
        this.lastUpdate = in.readLong();
        this.creatorName = in.readString();
        this.creatorDomain = in.readString();
        custom = in.readBundle();
        int tmpReportingState = in.readInt();
        this.reportingState = tmpReportingState == -1 ? null : ReportingState.values()[tmpReportingState];
        this.reportingStateHistory = new ArrayList<>();
        in.readList(this.reportingStateHistory, List.class.getClassLoader());
        in.readParcelable(ITState.class.getClassLoader());
    }

    /**
     * Post a new comment message
     *
     * @param siteId             The linked ITSite's Intent id where to post the comment
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param message            Message of the comment
     */
    public static void postComment(Context context, String siteId, String activityCategoryId, String message, ITApiCallback<Void> callback) {
        postComment(context, siteId, activityCategoryId, null, message, callback);
    }

    /**
     * Post a new comment message with title
     *
     * @param siteId             The linked ITSite's Intent id where to post the comment
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param message            Message of the comment
     */
    public static void postComment(Context context, String siteId, String activityCategoryId, String title, String message, ITApiCallback<Void> callback) {
        getServiceInstance(context).postMessage("comment", new Service.PostBody("site", siteId, activityCategoryId, title, message)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Post a new report
     *
     * @param siteId             The linked ITSite's Intent id where to post the report
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param message            Message(description) of the report
     */
    public static void postReport(Context context, String siteId, String activityCategoryId, String message, ITApiCallback<Void> callback) {
        postReport(context, siteId, activityCategoryId, null, message, callback);
    }

    /**
     * Post a new report with title
     *
     * @param siteId             The linked ITSite's Intent id where to post the report
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param title              Title of the report
     * @param message            Message(description) of the report
     */
    public static void postReport(Context context, String siteId, String activityCategoryId, String title, String message, ITApiCallback<Void> callback) {
        getServiceInstance(context).postMessage("report", new Service.PostBody("site", siteId, activityCategoryId, title, message)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Update report state
     *
     * @param messageId report message uuid
     * @param newState  the new state to set
     */
    public static void updateReportState(Context context, String messageId, ReportingState newState, ITApiCallback<Void> callback) {
        String newStateString = newState.name();
        getServiceInstance(context).updateMessage(messageId, new Service.UpdateBody(newStateString)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Update message message
     *
     * @param messageId  uuid of the message to update
     * @param newMessage the new message
     */
    public static void updateMessage(Context context, String messageId, String newMessage, ITApiCallback<Void> callback) {
        updateMessage(context, messageId, null, newMessage, callback);
    }

    /**
     * Update message title and/or message
     *
     * @param messageId  uuid of the message to update
     * @param newTitle   the new title
     * @param newMessage the new message
     */
    public static void updateMessage(Context context, String messageId, String newTitle, String newMessage, ITApiCallback<Void> callback) {
        getServiceInstance(context).updateMessage(messageId, new Service.UpdateBody(newTitle, newMessage)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Get unread messages count by site Id with activityCategory filter
     *
     * @param siteId              The ITSite's Intent id
     * @param activityCategoryIds List of activityCategories for filter
     */
    public static void getUnreadMessagesCountBySiteIdAndActivityCategory(Context context, String siteId, List<String> activityCategoryIds, ITApiCallback<ITMessagesCount.ByActivityCategory> callback) {
        getServiceInstance(context).getUnreadMessagesCountByTheme(activityCategoryIds, "site", siteId, 0).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Get unread messages count by theme for siteId
     *
     * @param siteId The ITSite's Intent id
     */
    public static void getUnreadMessagesCountByActivityCategory(Context context, String siteId, ITApiCallback<ITMessagesCount.ByActivityCategory> callback) {
        getServiceInstance(context).getUnreadMessagesCountByTheme(null, "site", siteId, 0).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Get unread messages count by site Ids
     *
     * @param siteIds The ITSite's Intent ids
     */
    public static void getUnreadMessagesCountBySite(Context context, List<String> siteIds, ITApiCallback<ITMessagesCount.ByAssetId> callback) {
        getUnreadMessagesCountBySite(context, siteIds, 0, callback);
    }

    /**
     * Get unread messages count by site Ids
     *
     * @param siteIds      The ITSite's Intent ids
     * @param updatedSince Count only messages updated since this date
     */
    public static void getUnreadMessagesCountBySite(Context context, List<String> siteIds, long updatedSince, ITApiCallback<ITMessagesCount.ByAssetId> callback) {
        getServiceInstance(context).getUnreadMessagesCountByAsset(null, "site", siteIds, updatedSince).enqueue(new ProxyCallback<>(callback));
    }

    private static Service getServiceInstance(Context context) {
        if (sService == null) {
            sService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
        }
        return sService;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.message);
        dest.writeByte(removed ? (byte) 1 : (byte) 0);
        dest.writeLong(this.creationDate);
        dest.writeLong(this.lastUpdate);
        dest.writeString(this.creatorName);
        dest.writeString(this.creatorDomain);
        dest.writeBundle(custom);
        dest.writeInt(this.reportingState == null ? -1 : this.reportingState.ordinal());
        dest.writeList(this.reportingStateHistory);
        dest.writeParcelable(this.state, flags);
    }


    public enum MessageType {
        @SerializedName("COMMENT")COMMENT,
        @SerializedName("REPORTING")REPORTING,
        @SerializedName("STATE")STATE
    }

    public enum ReportingState {
        @SerializedName("OPEN")OPEN,
        @SerializedName("CLOSED")CLOSED,
        @SerializedName("PROCESSING")PROCESSING
    }

    private interface Service {
        @POST("reports/v1/{messageType}/")
        Call<Void> postMessage(@Path("messageType") String messageType, @retrofit2.http.Body ITMessage.Service.PostBody body);

        @PUT("reports/v1/update/{messageId}")
        Call<Void> updateMessage(@Path("messageId") String messageId, @retrofit2.http.Body ITMessage.Service.UpdateBody body);

        @GET("reports/v1/unreadMessagesCountByTheme")
        Call<ITMessagesCount.ByActivityCategory> getUnreadMessagesCountByTheme(@Query("theme") List<String> theme, @Query("assetType") String assetType, @Query("assetId") String assetId, @Query("updatedSince") long updatedSince);

        @GET("reports/v1/unreadMessagesCountByAsset")
        Call<ITMessagesCount.ByAssetId> getUnreadMessagesCountByAsset(@Query("theme") List<String> theme, @Query("assetType") String assetType, @Query("assetIds") List<String> assetIds, @Query("updatedSince") long updatedSince);

        class PostBody {
            public String assetType;
            public String assetId;
            @SerializedName("theme")
            public String activityCategoryId;
            public String title;
            public String message;

            public PostBody(String assetType, String assetId, String activityCategoryId, String title, String message) {
                this.assetType = assetType;
                this.assetId = assetId;
                this.activityCategoryId = activityCategoryId;
                this.title = title;
                this.message = message;
            }
        }

        class UpdateBody {
            public String title;
            public String message;
            public String state;

            public UpdateBody(String state) {
                this.state = state;
            }

            public UpdateBody(String title, String message) {
                this.title = title;
                this.message = message;
            }
        }
    }

    public static class ReportingStateHistoryItem {
        public ReportingState state;
        public long timestamp;
        @SerializedName("changer")
        public String changerUserId;
        public String changerName;
    }

    public static class Deserializer implements JsonDeserializer<ITMessage> {
        @Override
        public ITMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder().registerTypeAdapter(ITState.class, new ITState.Deserializer()).create();
            ITMessage message = gson.fromJson(json, typeOfT);

            //Payload is empty for COMMENT messages types
            if (MessageType.REPORTING.equals(message.type) || MessageType.STATE.equals(message.type)) {
                JsonObject payload = json.getAsJsonObject().getAsJsonObject("payload");
                if (payload != null) {
                    if (MessageType.REPORTING.equals(message.type)) {
                        message.reportingState = gson.fromJson(payload.get("state"), ReportingState.class);
                        message.reportingStateHistory = gson.fromJson(payload.getAsJsonArray("stateHistory"),
                                new TypeToken<ArrayList<ReportingStateHistoryItem>>() {
                                }.getType());
                    } else if (MessageType.STATE.equals(message.type)) {
                        message.state = gson.fromJson(payload, new TypeToken<ITState>() {
                        }.getType());
                    }
                }
            }

            return message;
        }
    }
}
