package eu.intent.sdk.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.internal.ProxyCallback;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A subscription to notifications. Notifications can be emails, SMS, or native push notifications.
 */
public class ITSubscription implements Parcelable {
    public static final Parcelable.Creator<ITSubscription> CREATOR = new Parcelable.Creator<ITSubscription>() {
        public ITSubscription createFromParcel(Parcel source) {
            return new ITSubscription(source);
        }

        public ITSubscription[] newArray(int size) {
            return new ITSubscription[size];
        }
    };

    private static Service sService;

    @SerializedName("type")
    public Category category;
    /**
     * If null, the user has no right to subscribe to email notifications.
     */
    @SerializedName("mail")
    public String[] emails;
    /**
     * If null, the user has no right to subscribe to push notifications.
     */
    public PushToken[] push;
    /**
     * If null, the user has no right to subscribe to SMS notifications.
     */
    public String[] sms;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITSubscription() {
        emails = new String[0];
        push = new PushToken[0];
        sms = new String[0];
    }

    protected ITSubscription(Parcel in) {
        int tmpType = in.readInt();
        category = tmpType == -1 ? null : Category.values()[tmpType];
        emails = in.createStringArray();
        push = in.createTypedArray(PushToken.CREATOR);
        sms = in.createStringArray();
        custom = in.readBundle();
    }

    /**
     * Retrieves the subscriptions of the user. If a subscription type (emails, push, sms) is null, the user has no right to subscribe to this type. If a subscription type is empty, the user can subscribe.
     *
     * @param categories and array of ITSubscription.Category you're interested in
     */
    public static void get(Context context, Category[] categories, ITApiCallback<List<ITSubscription>> callback) {
        String[] categoryNames = new String[categories.length];
        for (int i = 0; i < categories.length; i++) {
            categoryNames[i] = categories[i].toString();
        }
        getServiceInstance(context).get(categoryNames).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Subscribes to email notifications. The user will not be able to subscribe if the property emails is null.
     *
     * @param category the notification category
     * @param email    an email address the notifications will be send to
     */
    public static void subscribeToEmailNotifications(Context context, Category category, String email, ITApiCallback<Void> callback) {
        getServiceInstance(context).subscribeToEmailNotifications(category.toString(), new Service.Email(email)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Subscribes to GCM Push notifications. The user will not be able to subscribe if the property push is null.
     *
     * @param category   the notification category
     * @param instanceId an ID created with InstanceID
     * @param token      a token sent by GCM
     */
    public static void subscribeToGcmPushNotifications(Context context, Category category, String instanceId, String token, ITApiCallback<Void> callback) {
        getServiceInstance(context).subscribeToGcmPushNotifications(category.toString(), new PushToken(Platform.ANDROID, instanceId, token)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Subscribes to SMS notifications. The user will not be able to subscribe if the property sms is null.
     *
     * @param category    the notification category
     * @param phoneNumber a phone number the notifications will be send to
     */
    public static void subscribeToSmsNotifications(Context context, Category category, String phoneNumber, ITApiCallback<Void> callback) {
        getServiceInstance(context).subscribeToSmsNotifications(category.toString(), new Service.Sms(phoneNumber)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Unsubscribes from email notifications.
     *
     * @param category the category of the notifications you want to unsubscribe from
     * @param email    the email address you want to unsubscribe
     */
    public static void unsubscribeFromEmailNotifications(Context context, Category category, String email, ITApiCallback<Void> callback) {
        getServiceInstance(context).unsubscribeFromEmailNotifications(category.toString(), new String[]{email}).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Unsubscribes from GCM Push notifications.
     *
     * @param category   the category of the notifications you want to unsubscribe from
     * @param instanceId the ID you want to unsubscribe
     */
    public static void unsubscribeFromGcmPushNotifications(Context context, Category category, String instanceId, ITApiCallback<Void> callback) {
        getServiceInstance(context).unsubscribeFromGcmPushNotifications(category.toString(), new String[]{instanceId}).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Unsubscribes from SMS notifications.
     *
     * @param category    the category of the notifications you want to unsubscribe from
     * @param phoneNumber the phone number you want to unsubscribe
     */
    public static void unsubscribeFromSmsNotifications(Context context, Category category, String phoneNumber, ITApiCallback<Void> callback) {
        getServiceInstance(context).unsubscribeFromSmsNotifications(category.toString(), new String[]{phoneNumber}).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Registers a GCM token for push notifications. If this device is already registered, its GCM token will be updated with the new one.
     *
     * @param instanceId the instance ID for which you want to update the token
     * @param token      the new token provided by Google
     */
    public static void registerGcmToken(Context context, String instanceId, String token, ITApiCallback<Void> callback) {
        getServiceInstance(context).registerGcmToken(instanceId, new Service.Token(token)).enqueue(new ProxyCallback<>(callback));
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
        dest.writeInt(category == null ? -1 : category.ordinal());
        dest.writeStringArray(emails);
        dest.writeTypedArray(push, flags);
        dest.writeStringArray(sms);
        dest.writeBundle(custom);
    }

    public enum Category implements Parcelable {
        /**
         * A notification is sent when a new classified ad is published and is visible to the user.
         */
        @SerializedName("classified")CLASSIFIED_AD("classified"),
        /**
         * A notification is sent when a classified ad of the user is about to expire.
         */
        @SerializedName("classified_expiry")CLASSIFIED_AD_EXPIRY("classified_expiry"),
        /**
         * A notification is sent when someone is interested in a classified ad of the user.
         */
        @SerializedName("classified_answer")CLASSIFIED_AD_REPLY("classified_answer"),
        /**
         * A notification is sent when a news is published for the site of the user.
         */
        @SerializedName("news")NEWS("news"),
        UNKNOWN("unknown");

        public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
            public Category createFromParcel(Parcel source) {
                return Category.values()[source.readInt()];
            }

            public Category[] newArray(int size) {
                return new Category[size];
            }
        };

        private String key;

        Category(String key) {
            this.key = key;
        }

        public static Category fromString(String key) {
            if (!TextUtils.isEmpty(key)) {
                for (Category category : values()) {
                    if (TextUtils.equals(category.key, key)) {
                        return category;
                    }
                }
            }
            return UNKNOWN;
        }

        @Override
        public String toString() {
            return key;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ordinal());
        }
    }

    public enum Platform {
        ANDROID, IOS, UNKNOWN
    }

    private interface Service {
        @GET("notifications/v1/subscriptions")
        Call<List<ITSubscription>> get(@Query("types") String[] categories);

        @POST("notifications/v1/{type}/mail")
        Call<Void> subscribeToEmailNotifications(@Path("type") String category, @Body Email email);

        @POST("notifications/v1/{type}/push")
        Call<Void> subscribeToGcmPushNotifications(@Path("type") String category, @Body PushToken token);

        @POST("notifications/v1/{type}/sms")
        Call<Void> subscribeToSmsNotifications(@Path("type") String category, @Body Sms sms);

        @DELETE("notifications/v1/{type}/mail")
        Call<Void> unsubscribeFromEmailNotifications(@Path("type") String category, @Query("mail") String[] emails);

        @DELETE("notifications/v1/{type}/push")
        Call<Void> unsubscribeFromGcmPushNotifications(@Path("type") String category, @Query("push") String[] deviceIds);

        @DELETE("notifications/v1/{type}/sms")
        Call<Void> unsubscribeFromSmsNotifications(@Path("type") String category, @Query("sms") String[] phoneNumbers);

        @PUT("notifications/v1/registration/{deviceId}")
        Call<Void> registerGcmToken(@Path("deviceId") String deviceId, @Body Token token);

        class Email {
            @SerializedName("mail")
            public String email;
            public String locale;

            public Email(String email) {
                this.email = email;
                locale = Locale.getDefault().getLanguage();
            }
        }

        class Sms {
            @SerializedName("sms")
            public String phoneNumber;
            public String locale;

            public Sms(String phoneNumber) {
                this.phoneNumber = phoneNumber;
                locale = Locale.getDefault().getLanguage();
            }
        }

        class Token {
            @SerializedName("registrationId")
            public String token;

            public Token(String token) {
                this.token = token;
            }
        }
    }

    public static class PushToken implements Parcelable {
        public static final Creator<PushToken> CREATOR = new Creator<PushToken>() {
            public PushToken createFromParcel(Parcel source) {
                return new PushToken(source);
            }

            public PushToken[] newArray(int size) {
                return new PushToken[size];
            }
        };

        public String deviceId;
        @SerializedName("deviceType")
        public Platform platform;
        @SerializedName("registrationId")
        public String token;
        public String locale;

        public PushToken(Platform platform, String deviceId, String token) {
            this.platform = platform;
            this.deviceId = deviceId;
            this.token = token;
            locale = Locale.getDefault().getLanguage();
        }

        protected PushToken(Parcel in) {
            platform = Platform.values()[in.readInt()];
            deviceId = in.readString();
            token = in.readString();
            locale = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(platform.ordinal());
            dest.writeString(deviceId);
            dest.writeString(token);
            dest.writeString(locale);
        }
    }
}
