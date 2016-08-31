package eu.intent.sdk.api;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import eu.intent.sdk.api.internal.ProxyCallback;
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
     * Gets an ITConversation for the given site and activity category.
     *
     * @param siteId             the ID of an ITSite
     * @param activityCategoryId the ID of an ITActivityCategory
     */
    public void getBySiteAndActivityCategory(String siteId, String activityCategoryId, ITApiCallback<ITConversation> callback) {
        getBySiteAndActivityCategory(siteId, activityCategoryId, 0, callback);
    }

    /**
     * Gets an ITConversation for the given site and activity category. It will contain the messages for this conversation, that have been sent after the given timestamp.
     *
     * @param siteId             the ID of an ITSite
     * @param activityCategoryId the ID of an ITActivityCategory
     * @param since              the conversation will contain the message received after this timestamp
     */
    public void getBySiteAndActivityCategory(String siteId, String activityCategoryId, long since, ITApiCallback<ITConversation> callback) {
        mService.get(activityCategoryId, "site", siteId, since).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets an ITConversation for the given site and activity category, paginated.
     *
     * @param siteId             the ID of an ITSite
     * @param activityCategoryId the ID of an ITActivityCategory
     * @param page               the page you want to access (starting at 1)
     * @param countByPage        the number of messages you want
     */
    public void getBySiteAndActivityCategoryPaginated(String siteId, String activityCategoryId, int page, int countByPage, ITApiCallback<ITConversation> callback) {
        mService.get(activityCategoryId, "site", siteId, page, countByPage).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Marks the given message as read.
     *
     * @param messageId the ID of an ITMessage
     */
    public void markAsRead(String messageId, ITApiCallback<Void> callback) {
        mService.markAsRead(messageId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Posts a new comment message in the conversation of the given site and activity category.
     *
     * @param siteId             the ID of an ITSite
     * @param activityCategoryId the ID of an ITActivityCategory
     * @param text               the text that will be sent to the conversation
     */
    public void postComment(String siteId, String activityCategoryId, String text, ITApiCallback<Void> callback) {
        postComment(siteId, activityCategoryId, null, text, callback);
    }

    /**
     * Posts a new message with a title, in the conversation of the given site and activity category.
     *
     * @param siteId             the ID of an ITSite
     * @param activityCategoryId the ID of an ITActivityCategory
     * @param title              the title of the message
     * @param text               the content of the message
     */
    public void postComment(String siteId, String activityCategoryId, String title, String text, ITApiCallback<Void> callback) {
        mService.postMessage("comment", new Service.PostBody("site", siteId, activityCategoryId, title, text)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Posts a new report in the conversation of the given site and activity category.
     *
     * @param siteId             the ID of an ITSite
     * @param activityCategoryId the ID of an ITActivityCategory
     * @param text               the text of the report
     */
    public void postReport(String siteId, String activityCategoryId, String text, ITApiCallback<Void> callback) {
        postReport(siteId, activityCategoryId, null, text, callback);
    }

    /**
     * Posts a new report with a title, in the conversation of the given site and activity category.
     *
     * @param siteId             the ID of an ITSite
     * @param activityCategoryId the ID of an ITActivityCategory
     * @param title              the title of the report
     * @param text               the content of the report
     */
    public void postReport(String siteId, String activityCategoryId, String title, String text, ITApiCallback<Void> callback) {
        mService.postMessage("report", new Service.PostBody("site", siteId, activityCategoryId, title, text)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Updates a report state.
     *
     * @param messageId the ID of the report (ITMessage)
     * @param newState  the new state to set to the report
     */
    public void updateReportState(String messageId, ITMessage.ReportingState newState, ITApiCallback<Void> callback) {
        String newStateString = newState.name();
        mService.updateMessage(messageId, new Service.UpdateBody(newStateString)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Updates the text of a message.
     *
     * @param messageId the ID of the ITMessage to update
     * @param newText   the new content of the ITMessage
     */
    public void updateMessage(String messageId, String newText, ITApiCallback<Void> callback) {
        updateMessage(messageId, null, newText, callback);
    }

    /**
     * Updates the text and title of a message.
     *
     * @param messageId the ID of the ITMessage to update
     * @param newTitle  the new title of the ITMessage
     * @param newText   the new content of the ITMessage
     */
    public void updateMessage(String messageId, String newTitle, String newText, ITApiCallback<Void> callback) {
        mService.updateMessage(messageId, new Service.UpdateBody(newTitle, newText)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets the number of unread messages for the given equipment and activity category.
     *
     * @param equipmentId         the ID of an ITEquipment
     * @param activityCategoryIds the IDs of several ITActivityCategories
     */
    public void getUnreadMessagesCountByEquipmentIdAndActivityCategories(String equipmentId, List<String> activityCategoryIds, ITApiCallback<ITMessagesCount.ByActivityCategory> callback) {
        mService.getUnreadMessagesCountByTheme(activityCategoryIds, "equip", equipmentId, 0).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets the number of unread messages for the given site and activity category.
     *
     * @param siteId              the ID of an ITSite
     * @param activityCategoryIds the IDs of several ITActivityCategories
     */
    public void getUnreadMessagesCountBySiteIdAndActivityCategories(String siteId, List<String> activityCategoryIds, ITApiCallback<ITMessagesCount.ByActivityCategory> callback) {
        mService.getUnreadMessagesCountByTheme(activityCategoryIds, "site", siteId, 0).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets the number of unread messages for the given equipment, grouped by activity categories.
     *
     * @param equipmentId the ID of an ITEquipment
     */
    public void getUnreadMessagesCountByEquipmentIdGroupedByActivityCategory(String equipmentId, ITApiCallback<ITMessagesCount.ByActivityCategory> callback) {
        mService.getUnreadMessagesCountByTheme(null, "equip", equipmentId, 0).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets the number of unread messages for the given site, grouped by activity categories.
     *
     * @param siteId the ID of an ITSite
     */
    public void getUnreadMessagesCountBySiteIdGroupedByActivityCategory(String siteId, ITApiCallback<ITMessagesCount.ByActivityCategory> callback) {
        mService.getUnreadMessagesCountByTheme(null, "site", siteId, 0).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets the number of unread messages for the given sites.
     *
     * @param siteIds the IDs of multiple ITSites
     */
    public void getUnreadMessagesCountBySites(List<String> siteIds, ITApiCallback<ITMessagesCount.ByAssetId> callback) {
        getUnreadMessagesCountBySites(siteIds, 0, callback);
    }

    /**
     * Gets the number of unread messages since the given timestamp for the given sites.
     *
     * @param siteIds the IDs of multiple ITSites
     * @param since   count the unread messages since this timestamp
     */
    public void getUnreadMessagesCountBySites(List<String> siteIds, long since, ITApiCallback<ITMessagesCount.ByAssetId> callback) {
        mService.getUnreadMessagesCountByAsset(null, "site", siteIds, since).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets the number of unread messages for the given equipments.
     *
     * @param equipmentIds the IDs of multiple ITEquipments.
     */
    public void getUnreadMessagesCountByEquipments(List<String> equipmentIds, ITApiCallback<ITMessagesCount.ByAssetId> callback) {
        getUnreadMessagesCountByEquipments(equipmentIds, 0, callback);
    }

    /**
     * Gets the number of unread messages since the given timestamp for the given equipments.
     *
     * @param equipmentIds the IDs of multiple ITEquipments
     * @param since        count the unread messages since this timestamp
     */
    public void getUnreadMessagesCountByEquipments(List<String> equipmentIds, long since, ITApiCallback<ITMessagesCount.ByAssetId> callback) {
        mService.getUnreadMessagesCountByAsset(null, "equip", equipmentIds, since).enqueue(new ProxyCallback<>(callback));
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
