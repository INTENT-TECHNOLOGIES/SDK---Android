package eu.intent.sdk.api;

import java.util.List;

import eu.intent.sdk.model.ITConversation;
import eu.intent.sdk.model.ITMessagesCount;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITReportingApi {
    @GET("reports/v1/")
    Call<ITConversation> get(@Query("theme") String theme, @Query("assetType") String assetType, @Query("assetId") String assetId, @Query("page") int page, @Query("countByPage") int countByPage, @Query("since") long updatedSince);

    @POST("reports/v1/markAsRead/{messageId}")
    Call<Void> markAsRead(@Path("messageId") String messageId);

    @POST("reports/v1/{messageType}/")
    Call<Void> postMessage(@Path("messageType") String messageType, @Body NewPost body);

    @PUT("reports/v1/report/{messageId}")
    Call<Void> updateMessage(@Path("messageId") String messageId, @Body UpdatePost body);

    @GET("reports/v1/unreadMessagesCountByTheme")
    Call<ITMessagesCount.ByTheme> getUnreadMessagesCountByTheme(@Query("theme") List<String> theme, @Query("assetType") String assetType, @Query("assetId") String assetId, @Query("updatedSince") long updatedSince);

    @GET("reports/v1/unreadMessagesCountByAsset")
    Call<ITMessagesCount.ByAssetId> getUnreadMessagesCountByAsset(@Query("theme") List<String> theme, @Query("assetType") String assetType, @Query("assetIds") List<String> assetIds, @Query("updatedSince") long updatedSince);

    class NewPost {
        public String assetType;
        public String assetId;
        public String theme;
        public String title;
        public String message;

        public NewPost(String assetType, String assetId, String theme, String title, String message) {
            this.assetType = assetType;
            this.assetId = assetId;
            this.theme = theme;
            this.title = title;
            this.message = message;
        }
    }

    class UpdatePost {
        public String title;
        public String message;
        public String state;

        public UpdatePost(String state) {
            this.state = state;
        }

        public UpdatePost(String title, String message) {
            this.title = title;
            this.message = message;
        }
    }
}