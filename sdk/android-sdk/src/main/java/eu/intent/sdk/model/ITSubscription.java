package eu.intent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

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
