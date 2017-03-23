package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * A message count includes alerts (non default states), open reports, and comments (messages).
 */
public class ITMessagesCount {

    /**
     * Messages grouped by theme.
     */
    public static class ByTheme implements Parcelable {
        public static final Parcelable.Creator<ByTheme> CREATOR = new Parcelable.Creator<ByTheme>() {
            public ByTheme createFromParcel(Parcel source) {
                return new ByTheme(source);
            }

            public ByTheme[] newArray(int size) {
                return new ByTheme[size];
            }
        };
        /**
         * theme <=> counts
         */
        public HashMap<String, Item> countsByTheme = new HashMap<>();


        public ByTheme() {
            // Needed by Gson
        }

        protected ByTheme(Parcel in) {
            Bundle messagesCountsBundle = in.readBundle();
            for (String assetId : messagesCountsBundle.keySet()) {
                countsByTheme.put(assetId, messagesCountsBundle.<Item>getParcelable(assetId));
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            Bundle messagesCountsBundle = new Bundle();
            for (Map.Entry<String, Item> messageCount : countsByTheme.entrySet()) {
                messagesCountsBundle.putParcelable(messageCount.getKey(), messageCount.getValue());
            }
            dest.writeBundle(messagesCountsBundle);
        }

        public static class Deserializer implements JsonDeserializer<ByTheme> {

            @Override
            public ByTheme deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                ByTheme messagesCounts = new ByTheme();
                JsonArray messageCountsArray = json.getAsJsonArray();

                Gson gson = new Gson();
                for (int i = 0; i < messageCountsArray.size(); i++) {
                    JsonObject item = messageCountsArray.get(i).getAsJsonObject();

                    String activityCategory = item.get("theme").getAsString();
                    Item countItem = gson.fromJson(item, Item.class);

                    messagesCounts.countsByTheme.put(activityCategory, countItem);
                }

                return messagesCounts;
            }
        }
    }

    /**
     * Messages grouped by asset.
     */
    public static class ByAssetId implements Parcelable {
        public static final Parcelable.Creator<ByAssetId> CREATOR = new Parcelable.Creator<ByAssetId>() {
            public ByAssetId createFromParcel(Parcel source) {
                return new ByAssetId(source);
            }

            public ByAssetId[] newArray(int size) {
                return new ByAssetId[size];
            }
        };
        /**
         * assetId <=> counts
         */
        public HashMap<String, Item> countsByAssetId = new HashMap<>();


        public ByAssetId() {
            // Needed by Gson
        }

        protected ByAssetId(Parcel in) {
            Bundle messagesCountsBundle = in.readBundle();
            for (String assetId : messagesCountsBundle.keySet()) {
                countsByAssetId.put(assetId, messagesCountsBundle.<Item>getParcelable(assetId));
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            Bundle messagesCountsBundle = new Bundle();
            for (Map.Entry<String, Item> messageCount : countsByAssetId.entrySet()) {
                messagesCountsBundle.putParcelable(messageCount.getKey(), messageCount.getValue());
            }
            dest.writeBundle(messagesCountsBundle);
        }

        public static class Deserializer implements JsonDeserializer<ByAssetId> {

            @Override
            public ByAssetId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                ByAssetId messagesCounts = new ByAssetId();
                JsonArray messageCountsArray = json.getAsJsonArray();

                Gson gson = new Gson();
                for (int i = 0; i < messageCountsArray.size(); i++) {
                    JsonObject item = messageCountsArray.get(i).getAsJsonObject();

                    String assetId = item.get("assetId").getAsString();
                    Item countItem = gson.fromJson(item, Item.class);

                    messagesCounts.countsByAssetId.put(assetId, countItem);
                }

                return messagesCounts;
            }
        }
    }

    public static class Item implements Parcelable {
        public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
            public Item createFromParcel(Parcel source) {
                return new Item(source);
            }

            public Item[] newArray(int size) {
                return new Item[size];
            }
        };
        /**
         * Count of messages (includes report and states messages), default to -1
         */
        public int messageCount = -1;
        /**
         * Count of open reports, default to -1
         */
        @SerializedName("openReportingCount")
        public int openReportsCount = -1;
        /**
         * Count of non default states, default to -1
         */
        public int nonDefaultStateCount = -1;

        public Item() {
            // Needed by Gson
        }

        protected Item(Parcel in) {
            this.messageCount = in.readInt();
            this.openReportsCount = in.readInt();
            this.nonDefaultStateCount = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.messageCount);
            dest.writeInt(this.openReportsCount);
            dest.writeInt(this.nonDefaultStateCount);
        }
    }
}
