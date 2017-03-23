package eu.intent.sdk.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Locale;

import eu.intent.sdk.model.ITSubscription;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITNotificationApi {
    @GET("notifications/v1/subscriptions")
    Call<List<ITSubscription>> get(@Query("types") String... categories);

    @POST("notifications/v1/{type}/mail")
    Call<Void> subscribeToEmailNotifications(@Path("type") String category, @Body Email email);

    @POST("notifications/v1/{type}/push")
    Call<Void> subscribeToGcmPushNotifications(@Path("type") String category, @Body ITSubscription.PushToken token);

    @POST("notifications/v1/{type}/sms")
    Call<Void> subscribeToSmsNotifications(@Path("type") String category, @Body Sms sms);

    @DELETE("notifications/v1/{type}/mail")
    Call<Void> unsubscribeFromEmailNotifications(@Path("type") String category, @Query("mail") String... emails);

    @DELETE("notifications/v1/{type}/push")
    Call<Void> unsubscribeFromGcmPushNotifications(@Path("type") String category, @Query("push") String... deviceIds);

    @DELETE("notifications/v1/{type}/sms")
    Call<Void> unsubscribeFromSmsNotifications(@Path("type") String category, @Query("sms") String... phoneNumbers);

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