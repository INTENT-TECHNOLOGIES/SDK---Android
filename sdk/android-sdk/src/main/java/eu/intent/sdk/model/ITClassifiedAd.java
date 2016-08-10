package eu.intent.sdk.model;

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
    }

    protected ITClassifiedAd(Parcel in) {
        category = in.readString();
        creationDate = in.readLong();
        expirationDate = in.readLong();
        hasMyReply = in.readByte() != 0;
        id = in.readString();
        isMine = in.readByte() != 0;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeLong(creationDate);
        dest.writeLong(expirationDate);
        dest.writeByte(hasMyReply ? (byte) 1 : (byte) 0);
        dest.writeString(id);
        dest.writeByte(isMine ? (byte) 1 : (byte) 0);
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
