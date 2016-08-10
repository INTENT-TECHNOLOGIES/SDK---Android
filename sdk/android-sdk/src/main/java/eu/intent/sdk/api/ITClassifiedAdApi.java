package eu.intent.sdk.api;

import android.content.Context;

import java.util.List;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITClassifiedAd;
import eu.intent.sdk.model.ITClassifiedAdCategory;
import eu.intent.sdk.model.ITClassifiedAdExpiry;
import eu.intent.sdk.model.ITUser;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A wrapper to call the Classified Ad API.
 */
public class ITClassifiedAdApi {
    private Service mService;

    public ITClassifiedAdApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Cancels the reply to the given classified ad from the current authenticated user.
     *
     * @param adId the ID of the classified ad to update
     */
    public void cancelReply(String adId, ITApiCallback<Void> callback) {
        mService.cancelReply(adId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets the classified ads available to the authenticated user.
     */
    public void get(ITApiCallback<List<ITClassifiedAd>> callback) {
        get(null, null, null, null, callback);
    }

    /**
     * Gets the classified ads available to the authenticated user.
     *
     * @param searchQuery a full text search query (matches the ads text content)
     */
    public void get(String searchQuery, ITApiCallback<List<ITClassifiedAd>> callback) {
        get(searchQuery, null, null, null, callback);
    }

    /**
     * Gets the classified ads available to the authenticated user.
     *
     * @param type          an ITClassifiedAd.Type if you only want demands or offers
     * @param onlyMyAds     true if you only want to getByAddress the ads published by the authenticated user
     * @param onlyMyReplies true if you only want to getByAddress the ads to whom the authenticated user replied
     */
    public void get(ITClassifiedAd.Type type, Boolean onlyMyAds, Boolean onlyMyReplies, ITApiCallback<List<ITClassifiedAd>> callback) {
        get(null, type, onlyMyAds, onlyMyReplies, callback);
    }

    /**
     * Gets the classified ads available to the authenticated user.
     *
     * @param searchQuery   a full text search query (matches the ads text content)
     * @param type          an ITClassifiedAd.Type if you only want demands or offers
     * @param onlyMyAds     true if you only want to getByAddress the ads published by the authenticated user
     * @param onlyMyReplies true if you only want to getByAddress the ads to whom the authenticated user replied
     */
    public void get(String searchQuery, ITClassifiedAd.Type type, Boolean onlyMyAds, Boolean onlyMyReplies, ITApiCallback<List<ITClassifiedAd>> callback) {
        mService.get(searchQuery, type == null ? null : type.toString(), onlyMyAds, onlyMyReplies).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets the categories of classified ads.
     */
    public void getCategories(ITApiCallback<List<ITClassifiedAdCategory>> callback) {
        mService.get().enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Publishes a new classified ad.
     *
     * @param publisher the user who publishes the ad
     * @param type      the Type of the ad
     * @param category  and ITClassifiedAdCategory
     * @param text      the description of the ad
     */
    public void publish(ITUser publisher, ITClassifiedAd.Type type, ITClassifiedAdCategory category, String text, ITApiCallback<ITClassifiedAd> callback) {
        Service.NewAd body = new Service.NewAd(publisher, type, category, text);
        mService.publish(body).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Renews the given classified ad, postponing its expiration date.
     *
     * @param adId the ID of the classified ad to renew
     */
    public void renew(String adId, ITApiCallback<ITClassifiedAdExpiry> callback) {
        mService.renew(adId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Replies to the given classified ad.
     *
     * @param adId the ID of the classified ad to reply to
     * @param user the user who replies to the ad
     */
    public void reply(String adId, ITUser user, ITApiCallback<Void> callback) {
        Service.User body = new Service.User(user);
        mService.reply(adId, body).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Updates the visibility of the given classified ad.
     *
     * @param adId       the ID of the classified ad to update
     * @param visibility the new Visibility of the classified ad
     */
    public void updateVisibility(String adId, ITClassifiedAd.Visibility visibility, ITApiCallback<Void> callback) {
        mService.updateVisibility(adId, visibility).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @POST("residentservices/v1/classifieds/{id}/cancel_answer")
        Call<Void> cancelReply(@Path("id") String adId);

        @GET("residentservices/v1/classifieds")
        Call<List<ITClassifiedAd>> get(@Query("search") String searchQuery, @Query("type") String type, @Query("myClassifiedsOnly") Boolean onlyMyAds, @Query("myResponsesOnly") Boolean onlyMyReplies);

        @GET("residentservices/v1/classifieds/categories")
        Call<List<ITClassifiedAdCategory>> get();

        @POST("residentservices/v1/classifieds")
        Call<ITClassifiedAd> publish(@retrofit2.http.Body NewAd body);

        @POST("residentservices/v1/classifieds/{id}/expirationDate")
        Call<ITClassifiedAdExpiry> renew(@Path("id") String adId);

        @POST("residentservices/v1/classifieds/{id}/answer")
        Call<Void> reply(@Path("id") String adId, @retrofit2.http.Body User body);

        @POST("residentservices/v1/classifieds/{id}/visibility")
        Call<Void> updateVisibility(@Path("id") String adId, @Query("visibility") ITClassifiedAd.Visibility visibility);

        class NewAd {
            public String category;
            public String creatorName;
            public String description;
            public String type;

            private NewAd(ITUser publisher, ITClassifiedAd.Type type, ITClassifiedAdCategory category, String text) {
                this.category = category.key;
                this.creatorName = publisher.getShortName();
                this.description = text;
                this.type = type.toString();
            }
        }

        class User {
            public String mail;
            public String name;
            public String phone;

            private User(ITUser user) {
                this.mail = user.email;
                this.name = user.getShortName();
                this.phone = user.getOnePhone();
            }
        }
    }
}
