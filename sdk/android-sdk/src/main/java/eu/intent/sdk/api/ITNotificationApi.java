package eu.intent.sdk.api;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

import eu.intent.sdk.api.internal.ProxyCallback;
import eu.intent.sdk.model.ITSubscription;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * A wrapper to call the Notification API.
 */
public class ITNotificationApi {
    private Service mService;

    public ITNotificationApi(Context context) {
        mService = ITRetrofitUtils.getRetrofitInstance(context).create(Service.class);
    }

    /**
     * Retrieves the subscriptions of the user. If a subscription type (emails, push, sms) is null, the user has no right to subscribe to this type. If a subscription type is empty, the user can subscribe.
     *
     * @param categories and array of ITSubscription.Category you're interested in
     */
    public void get(ITSubscription.Category[] categories, ITApiCallback<List<ITSubscription>> callback) {
        String[] categoryNames = new String[categories.length];
        for (int i = 0; i < categories.length; i++) {
            categoryNames[i] = categories[i].toString();
        }
        mService.get(categoryNames).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Subscribes to email notifications. The user will not be able to subscribe if the property emails is null.
     *
     * @param category the notification category
     * @param email    an email address the notifications will be send to
     */
    public void subscribeToEmailNotifications(ITSubscription.Category category, String email, ITApiCallback<Void> callback) {
        mService.subscribeToEmailNotifications(category.toString(), new Service.Email(email)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Subscribes to GCM Push notifications. The user will not be able to subscribe if the property push is null.
     *
     * @param category   the notification category
     * @param instanceId an ID created with InstanceID
     * @param token      a token sent by GCM
     */
    public void subscribeToGcmPushNotifications(ITSubscription.Category category, String instanceId, String token, ITApiCallback<Void> callback) {
        mService.subscribeToGcmPushNotifications(category.toString(), new ITSubscription.PushToken(ITSubscription.Platform.ANDROID, instanceId, token)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Subscribes to SMS notifications. The user will not be able to subscribe if the property sms is null.
     *
     * @param category    the notification category
     * @param phoneNumber a phone number the notifications will be send to
     */
    public void subscribeToSmsNotifications(ITSubscription.Category category, String phoneNumber, ITApiCallback<Void> callback) {
        mService.subscribeToSmsNotifications(category.toString(), new Service.Sms(phoneNumber)).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Unsubscribes from email notifications.
     *
     * @param category the category of the notifications you want to unsubscribe from
     * @param email    the email address you want to unsubscribe
     */
    public void unsubscribeFromEmailNotifications(ITSubscription.Category category, String email, ITApiCallback<Void> callback) {
        mService.unsubscribeFromEmailNotifications(category.toString(), new String[]{email}).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Unsubscribes from GCM Push notifications.
     *
     * @param category   the category of the notifications you want to unsubscribe from
     * @param instanceId the ID you want to unsubscribe
     */
    public void unsubscribeFromGcmPushNotifications(ITSubscription.Category category, String instanceId, ITApiCallback<Void> callback) {
        mService.unsubscribeFromGcmPushNotifications(category.toString(), new String[]{instanceId}).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Unsubscribes from SMS notifications.
     *
     * @param category    the category of the notifications you want to unsubscribe from
     * @param phoneNumber the phone number you want to unsubscribe
     */
    public void unsubscribeFromSmsNotifications(ITSubscription.Category category, String phoneNumber, ITApiCallback<Void> callback) {
        mService.unsubscribeFromSmsNotifications(category.toString(), new String[]{phoneNumber}).enqueue(new ProxyCallback<>(callback));
    }

    /**
     * Registers a GCM token for push notifications. If this device is already registered, its GCM token will be updated with the new one.
     *
     * @param instanceId the instance ID for which you want to update the token
     * @param token      the new token provided by Google
     */
    public void registerGcmToken(String instanceId, String token, ITApiCallback<Void> callback) {
        mService.registerGcmToken(instanceId, new Service.Token(token)).enqueue(new ProxyCallback<>(callback));
    }

    private interface Service {
        @GET("notifications/v1/subscriptions")
        Call<List<ITSubscription>> get(@Query("types") String[] categories);

        @POST("notifications/v1/{type}/mail")
        Call<Void> subscribeToEmailNotifications(@Path("type") String category, @Body Email email);

        @POST("notifications/v1/{type}/push")
        Call<Void> subscribeToGcmPushNotifications(@Path("type") String category, @Body ITSubscription.PushToken token);

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
}
