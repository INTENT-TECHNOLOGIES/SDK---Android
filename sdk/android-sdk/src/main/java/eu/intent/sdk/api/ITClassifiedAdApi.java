package eu.intent.sdk.api;

import java.util.List;

import eu.intent.sdk.model.ITClassifiedAd;
import eu.intent.sdk.model.ITClassifiedAdCategory;
import eu.intent.sdk.model.ITClassifiedAdExpiry;
import eu.intent.sdk.model.ITUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITClassifiedAdApi {
    @POST("residentservices/v1/classifieds/{id}/cancel_answer")
    Call<Void> cancelReply(@Path("id") String classifiedId);

    @GET("residentservices/v1/classifieds")
    Call<List<ITClassifiedAd>> get(@Query("search") String searchQuery, @Query("type") String type, @Query("myClassifiedsOnly") Boolean onlyMyClassifieds, @Query("myResponsesOnly") Boolean onlyMyReplies);

    @GET("residentservices/v1/classifieds/categories")
    Call<List<ITClassifiedAdCategory>> getCategories();

    @POST("residentservices/v1/classifieds")
    Call<ITClassifiedAd> publish(@Body NewClassified body);

    @POST("residentservices/v1/classifieds/{id}/expirationDate")
    Call<ITClassifiedAdExpiry> renew(@Path("id") String classifiedId);

    @POST("residentservices/v1/classifieds/{id}/answer")
    Call<Void> reply(@Path("id") String classifiedId, @Body User body);

    @POST("residentservices/v1/classifieds/{id}/visibility")
    Call<Void> updateVisibility(@Path("id") String classifiedId, @Query("visibility") ITClassifiedAd.Visibility visibility);

    final class NewClassified {
        public String category;
        public String creatorName;
        public String description;
        public String type;

        public NewClassified(ITUser publisher, ITClassifiedAd.Type type, ITClassifiedAdCategory category, String text) {
            this.category = category.key;
            this.creatorName = publisher.getShortName();
            this.description = text;
            this.type = type.toString();
        }
    }

    final class User {
        public String mail;
        public String name;
        public String phone;

        public User(ITUser user) {
            this.mail = user.email;
            this.name = user.getShortName();
            this.phone = user.getOnePhone();
        }
    }
}
