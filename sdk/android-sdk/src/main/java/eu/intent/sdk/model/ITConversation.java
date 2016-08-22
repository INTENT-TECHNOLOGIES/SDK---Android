package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A conversation linked to a site and {@link ITActivityCategory}
 */
public class ITConversation implements Parcelable {
    public static final Parcelable.Creator<ITConversation> CREATOR = new Parcelable.Creator<ITConversation>() {
        public ITConversation createFromParcel(Parcel source) {
            return new ITConversation(source);
        }

        public ITConversation[] newArray(int size) {
            return new ITConversation[size];
        }
    };

    public String assetId;
    public ITAssetType assetType;
    public String activityCategory;
    public List<ITMessage> messages;
    public int totalMessagesCount;
    public long lastUpdate;
    public long creationDate;
    public long lastRead;
    public String creatorDomain;
    public String creatorName;
    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    public Bundle custom = new Bundle();

    public ITConversation() {
        // Needed by Retrofit
    }

    protected ITConversation(Parcel in) {
        assetId = in.readString();
        int tmpAssetType = in.readInt();
        assetType = tmpAssetType == -1 ? null : ITAssetType.values()[tmpAssetType];
        activityCategory = in.readString();
        messages = new ArrayList<>();
        in.readList(this.messages, List.class.getClassLoader());
        totalMessagesCount = in.readInt();
        lastUpdate = in.readLong();
        creationDate = in.readLong();
        lastRead = in.readLong();
        creatorDomain = in.readString();
        creatorName = in.readString();
        custom = in.readBundle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(assetId);
        dest.writeInt(assetType == null ? -1 : assetType.ordinal());
        dest.writeString(this.activityCategory);
        dest.writeList(this.messages);
        dest.writeInt(this.totalMessagesCount);
        dest.writeLong(this.lastUpdate);
        dest.writeLong(this.creationDate);
        dest.writeLong(this.lastRead);
        dest.writeString(this.creatorDomain);
        dest.writeString(this.creatorName);
        dest.writeBundle(custom);
    }

    public static class Deserializer implements JsonDeserializer<ITConversation> {
        @Override
        public ITConversation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder().registerTypeAdapter(ITMessage.class, new ITMessage.Deserializer()).create();
            ITConversation conversation = gson.fromJson(json, typeOfT);
            JsonObject link = json.getAsJsonObject().getAsJsonObject("link");
            conversation.activityCategory = link.get("theme").getAsString();
            conversation.assetId = link.get("assetId").getAsString();
            conversation.assetType = ITAssetType.fromString(link.get("assetType").getAsString().toUpperCase(Locale.US));
            return conversation;
        }
    }
}
