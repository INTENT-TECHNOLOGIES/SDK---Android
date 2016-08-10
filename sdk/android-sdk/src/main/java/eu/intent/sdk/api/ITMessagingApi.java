package eu.intent.sdk.api;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITActivityCategory;
import eu.intent.sdk.model.ITConversation;
import eu.intent.sdk.model.ITMessage;
import eu.intent.sdk.model.ITMessagesCount;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A wrapper to call the Messaging / Reporting API.
 */
public class ITMessagingApi {
    private Service mService;

    public ITMessagingApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Get conversation
     *
     * @param siteId             the ITSite's Intent id
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     */
    public void getBySiteAndActivityCategory(String siteId, String activityCategoryId, ITApiCallback<ITConversation> callback) {
        getBySiteAndActivityCategory(siteId, activityCategoryId, 0, callback);
    }

    /**
     * Mark a conversation as read by siteId and activityCategory
     *
     * @param messageId Id of the last read message
     */
    public void markAsRead(String messageId, ITApiCallback<Void> callback) {
        mService.markAsRead(messageId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Get conversation with message since specified date
     *
     * @param siteId             the ITSite's Intent id
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param updatedSince       Getting messages from this timestamp
     */
    public void getBySiteAndActivityCategory(String siteId, String activityCategoryId, long updatedSince, ITApiCallback<ITConversation> callback) {
        mService.get(activityCategoryId, "site", siteId, updatedSince).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Get conversation with message by page
     *
     * @param siteId             the ITSite's Intent id
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param page               Messages page (start at 1)
     * @param countByPage        Messages count by page
     */
    public void getBySiteAndActivityCategoryPaginated(String siteId, String activityCategoryId, int page, int countByPage, ITApiCallback<ITConversation> callback) {
        mService.get(activityCategoryId, "site", siteId, page, countByPage).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Post a new comment message
     *
     * @param siteId             The linked ITSite's Intent id where to post the comment
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param message            Message of the comment
     */
    public void postComment(String siteId, String activityCategoryId, String message, ITApiCallback<Void> callback) {
        postComment(siteId, activityCategoryId, null, message, callback);
    }

    /**
     * Post a new comment message with title
     *
     * @param siteId             The linked ITSite's Intent id where to post the comment
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param message            Message of the comment
     */
    public void postComment(String siteId, String activityCategoryId, String title, String message, ITApiCallback<Void> callback) {
        mService.postMessage("comment", new Service.PostBody("site", siteId, activityCategoryId, title, message)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Post a new report
     *
     * @param siteId             The linked ITSite's Intent id where to post the report
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param message            Message(description) of the report
     */
    public void postReport(String siteId, String activityCategoryId, String message, ITApiCallback<Void> callback) {
        postReport(siteId, activityCategoryId, null, message, callback);
    }

    /**
     * Post a new report with title
     *
     * @param siteId             The linked ITSite's Intent id where to post the report
     * @param activityCategoryId Activity category id {@link ITActivityCategory}
     * @param title              Title of the report
     * @param message            Message(description) of the report
     */
    public void postReport(String siteId, String activityCategoryId, String title, String message, ITApiCallback<Void> callback) {
        mService.postMessage("report", new Service.PostBody("site", siteId, activityCategoryId, title, message)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Update report state
     *
     * @param messageId report message uuid
     * @param newState  the new state to set
     */
    public void updateReportState(String messageId, ITMessage.ReportingState newState, ITApiCallback<Void> callback) {
        String newStateString = newState.name();
        mService.updateMessage(messageId, new Service.UpdateBody(newStateString)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Update message message
     *
     * @param messageId  uuid of the message to update
     * @param newMessage the new message
     */
    public void updateMessage(String messageId, String newMessage, ITApiCallback<Void> callback) {
        updateMessage(messageId, null, newMessage, callback);
    }

    /**
     * Update message title and/or message
     *
     * @param messageId  uuid of the message to update
     * @param newTitle   the new title
     * @param newMessage the new message
     */
    public void updateMessage(String messageId, String newTitle, String newMessage, ITApiCallback<Void> callback) {
        mService.updateMessage(messageId, new Service.UpdateBody(newTitle, newMessage)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Get unread messages count by site Id with activityCategory filter
     *
     * @param siteId              The ITSite's Intent id
     * @param activityCategoryIds List of activityCategories for filter
     */
    public void getUnreadMessagesCountBySiteIdAndActivityCategory(String siteId, List<String> activityCategoryIds, ITApiCallback<ITMessagesCount.ByActivityCategory> callback) {
        mService.getUnreadMessagesCountByTheme(activityCategoryIds, "site", siteId, 0).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Get unread messages count by theme for siteId
     *
     * @param siteId The ITSite's Intent id
     */
    public void getUnreadMessagesCountByActivityCategory(String siteId, ITApiCallback<ITMessagesCount.ByActivityCategory> callback) {
        mService.getUnreadMessagesCountByTheme(null, "site", siteId, 0).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Get unread messages count by site Ids
     *
     * @param siteIds The ITSite's Intent ids
     */
    public void getUnreadMessagesCountBySite(List<String> siteIds, ITApiCallback<ITMessagesCount.ByAssetId> callback) {
        getUnreadMessagesCountBySite(siteIds, 0, callback);
    }

    /**
     * Get unread messages count by site Ids
     *
     * @param siteIds      The ITSite's Intent ids
     * @param updatedSince Count only messages updated since this date
     */
    public void getUnreadMessagesCountBySite(List<String> siteIds, long updatedSince, ITApiCallback<ITMessagesCount.ByAssetId> callback) {
        mService.getUnreadMessagesCountByAsset(null, "site", siteIds, updatedSince).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("reports/v1/")
        Call<ITConversation> get(@Query("theme") String theme, @Query("assetType") String assetType, @Query("assetId") String assetId, @Query("since") long updatedSince);

        @GET("reports/v1/")
        Call<ITConversation> get(@Query("theme") String theme, @Query("assetType") String assetType, @Query("assetId") String assetId, @Query("page") long page, @Query("countByPage") long countByPage);

        @POST("reports/v1/markAsRead/{messageId}")
        Call<Void> markAsRead(@Path("messageId") String theme);

        @POST("reports/v1/{messageType}/")
        Call<Void> postMessage(@Path("messageType") String messageType, @retrofit2.http.Body PostBody body);

        @PUT("reports/v1/update/{messageId}")
        Call<Void> updateMessage(@Path("messageId") String messageId, @retrofit2.http.Body UpdateBody body);

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
}
