package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.util.ITParcelableUtils;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * An occupant can read and publish classified ads, visible by the other occupants of his residence.
 *
 * @see ITClassifiedAdReply
 */
public class ITClassifiedAd implements Parcelable {
    public static final Parcelable.Creator<ITClassifiedAd> CREATOR = new Parcelable.Creator<ITClassifiedAd>() {
        public ITClassifiedAd createFromParcel(Parcel source) {
            return new ITClassifiedAd(source);
        }

        public ITClassifiedAd[] newArray(int size) {
            return new ITClassifiedAd[size];
        }
    };

    private static Service sService;

    public String category;
    public long creationDate;
    public long expirationDate;
    /**
     * True if the authenticated user replied to this ad
     */
    @SerializedName("responded")
    public boolean hasMyReply;
    @SerializedName("announceId")
    public String id;
    /**
     * True if the authenticated user published this ad
     */
    @SerializedName("creator")
    public boolean isMine;
    @SerializedName("creatorName")
    public String publisherName;
    @SerializedName("responses")
    public List<ITClassifiedAdReply> replies = new ArrayList<>();
    @SerializedName("description")
    public String text;
    public long updateDate;

    transient public Type type;
    transient public Visibility visibility;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITClassifiedAd() {
        // Needed by Retrofit
    }

    protected ITClassifiedAd(Parcel in) {
        category = in.readString();
        creationDate = in.readLong();
        expirationDate = in.readLong();
        hasMyReply = ITParcelableUtils.readBoolean(in);
        id = in.readString();
        isMine = ITParcelableUtils.readBoolean(in);
        publisherName = in.readString();
        replies = new ArrayList<>();
        in.readList(replies, ITClassifiedAdReply.class.getClassLoader());
        text = in.readString();
        int tmpType = in.readInt();
        type = tmpType == -1 ? null : Type.values()[tmpType];
        updateDate = in.readLong();
        int tmpVisibility = in.readInt();
        visibility = tmpVisibility == -1 ? null : Visibility.values()[tmpVisibility];
        custom = in.readBundle();
    }

    /**
     * Cancels the reply to the given classified ad from the current authenticated user.
     *
     * @param adId the ID of the classified ad to update
     */
    public static void cancelReply(Context context, String adId, ITApiCallback<Void> callback) {
        getServiceInstance(context).cancelReply(adId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Gets the classified ads available to the authenticated user.
     */
    public static void get(Context context, ITApiCallback<List<ITClassifiedAd>> callback) {
        get(context, null, null, null, null, callback);
    }

    /**
     * Gets the classified ads available to the authenticated user.
     *
     * @param searchQuery a full text search query (matches the ads text content)
     */
    public static void get(Context context, String searchQuery, ITApiCallback<List<ITClassifiedAd>> callback) {
        get(context, searchQuery, null, null, null, callback);
    }

    /**
     * Gets the classified ads available to the authenticated user.
     *
     * @param type          an ITClassifiedAd.Type if you only want demands or offers
     * @param onlyMyAds     true if you only want to getByAddress the ads published by the authenticated user
     * @param onlyMyReplies true if you only want to getByAddress the ads to whom the authenticated user replied
     */
    public static void get(Context context, Type type, Boolean onlyMyAds, Boolean onlyMyReplies, ITApiCallback<List<ITClassifiedAd>> callback) {
        get(context, null, type, onlyMyAds, onlyMyReplies, callback);
    }

    /**
     * Gets the classified ads available to the authenticated user.
     *
     * @param searchQuery   a full text search query (matches the ads text content)
     * @param type          an ITClassifiedAd.Type if you only want demands or offers
     * @param onlyMyAds     true if you only want to getByAddress the ads published by the authenticated user
     * @param onlyMyReplies true if you only want to getByAddress the ads to whom the authenticated user replied
     */
    public static void get(Context context, String searchQuery, Type type, Boolean onlyMyAds, Boolean onlyMyReplies, ITApiCallback<List<ITClassifiedAd>> callback) {
        getServiceInstance(context).get(searchQuery, type == null ? null : type.toString(), onlyMyAds, onlyMyReplies).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Publishes a new classified ad.
     *
     * @param publisher the user who publishes the ad
     * @param type      the Type of the ad
     * @param category  and ITClassifiedAdCategory
     * @param text      the description of the ad
     */
    public static void publish(Context context, ITUser publisher, Type type, ITClassifiedAdCategory category, String text, ITApiCallback<ITClassifiedAd> callback) {
        Service.NewAd body = new Service.NewAd(publisher, type, category, text);
        getServiceInstance(context).publish(body).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Renews the given classified ad, postponing its expiration date.
     *
     * @param adId the ID of the classified ad to renew
     */
    public static void renew(Context context, String adId, ITApiCallback<ITClassifiedAdExpiry> callback) {
        getServiceInstance(context).renew(adId).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Replies to the given classified ad.
     *
     * @param adId the ID of the classified ad to reply to
     * @param user the user who replies to the ad
     */
    public static void reply(Context context, String adId, ITUser user, ITApiCallback<Void> callback) {
        Service.User body = new Service.User(user);
        getServiceInstance(context).reply(adId, body).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Updates the visibility of the given classified ad.
     *
     * @param adId       the ID of the classified ad to update
     * @param visibility the new Visibility of the classified ad
     */
    public static void updateVisibility(Context context, String adId, Visibility visibility, ITApiCallback<Void> callback) {
        getServiceInstance(context).updateVisibility(adId, visibility).enqueue(new ProxyCallback<>(callback));
    }

    private static Service getServiceInstance(Context context) {
        synchronized (ITClassifiedAd.class) {
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
        dest.writeString(category);
        dest.writeLong(creationDate);
        dest.writeLong(expirationDate);
        ITParcelableUtils.writeBoolean(dest, hasMyReply);
        dest.writeString(id);
        ITParcelableUtils.writeBoolean(dest, isMine);
        dest.writeString(publisherName);
        dest.writeList(replies);
        dest.writeString(text);
        dest.writeInt(type == null ? -1 : type.ordinal());
        dest.writeLong(updateDate);
        dest.writeInt(visibility == null ? -1 : visibility.ordinal());
        dest.writeBundle(custom);
    }

    public enum Type {
        DEMAND, OFFER, UNKNOWN;

        @Override
        public String toString() {
            return name().toLowerCase(Locale.US);
        }
    }

    public enum Visibility {
        HIDDEN, PUBLIC, RESTRICTED;

        @Override
        public String toString() {
            return name().toLowerCase(Locale.US);
        }
    }

    private interface Service {
        @POST("residentservices/v1/classifieds/{id}/cancel_answer")
        Call<Void> cancelReply(@Path("id") String adId);

        @GET("residentservices/v1/classifieds")
        Call<List<ITClassifiedAd>> get(@Query("search") String searchQuery, @Query("type") String type, @Query("myClassifiedsOnly") Boolean onlyMyAds, @Query("myResponsesOnly") Boolean onlyMyReplies);

        @POST("residentservices/v1/classifieds")
        Call<ITClassifiedAd> publish(@retrofit2.http.Body NewAd body);

        @POST("residentservices/v1/classifieds/{id}/expirationDate")
        Call<ITClassifiedAdExpiry> renew(@Path("id") String adId);

        @POST("residentservices/v1/classifieds/{id}/answer")
        Call<Void> reply(@Path("id") String adId, @retrofit2.http.Body User body);

        @POST("residentservices/v1/classifieds/{id}/visibility")
        Call<Void> updateVisibility(@Path("id") String adId, @Query("visibility") Visibility visibility);

        final class NewAd {
            public String category;
            public String creatorName;
            public String description;
            public String type;

            private NewAd(ITUser publisher, Type type, ITClassifiedAdCategory category, String text) {
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

            private User(ITUser user) {
                this.mail = user.email;
                this.name = user.getShortName();
                this.phone = user.getOnePhone();
            }
        }
    }

    public static class Deserializer implements JsonDeserializer<ITClassifiedAd> {
        @Override
        public ITClassifiedAd deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ITClassifiedAd ad = new Gson().fromJson(json, typeOfT);
            try {
                ad.type = Type.valueOf(json.getAsJsonObject().get("type").getAsString().toUpperCase(Locale.US));
            } catch (IllegalArgumentException e) {
                ad.type = Type.UNKNOWN;
            }
            try {
                ad.visibility = Visibility.valueOf(json.getAsJsonObject().get("visibility").getAsString().toUpperCase(Locale.US));
            } catch (IllegalArgumentException e) {
                ad.visibility = Visibility.PUBLIC;
            }
            return ad;
        }
    }
}
