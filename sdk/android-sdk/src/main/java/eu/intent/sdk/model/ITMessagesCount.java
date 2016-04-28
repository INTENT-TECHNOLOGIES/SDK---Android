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
import java.util.HashSet;
import java.util.Map;

/**
 * Messages, open reports and non default states count by activityCategory id
 * Messages count includes report and states messages count
 */
public class ITMessagesCount {

    /**
     * Messages, open reports and non default states count by activityCategory id
     * Messages count includes report and states messages count
     */
    public static class ByActivityCategory implements Parcelable {
        /**
         * activityCategoryId <=> counts
         */
        public HashMap<String, Item> countsByActivityCategory = new HashMap<>();

        public static class Deserializer implements JsonDeserializer<ByActivityCategory> {

            @Override
            public ByActivityCategory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                ByActivityCategory messagesCounts = new ByActivityCategory();
                JsonArray messageCountsArray = json.getAsJsonArray();

                Gson gson = new Gson();
                for (int i = 0; i < messageCountsArray.size(); i++) {
                    JsonObject item = messageCountsArray.get(i).getAsJsonObject();

                    String activityCategory = item.get("theme").getAsString();
                    Item countItem = gson.fromJson(item, Item.class);

                    messagesCounts.countsByActivityCategory.put(activityCategory, countItem);
                }

                return messagesCounts;
            }
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            Bundle messagesCountsBundle = new Bundle();
            for (Map.Entry<String, Item> messageCount : countsByActivityCategory.entrySet()) {
                messagesCountsBundle.putParcelable(messageCount.getKey(), messageCount.getValue());
            }
            dest.writeBundle(messagesCountsBundle);
        }
        public ByActivityCategory() {
        }

        protected ByActivityCategory(Parcel in) {
            Bundle messagesCountsBundle = in.readBundle();
            for (String assetId : messagesCountsBundle.keySet()) {
                countsByActivityCategory.put(assetId, messagesCountsBundle.<Item>getParcelable(assetId));
            }
        }

        public static final Parcelable.Creator<ByActivityCategory> CREATOR = new Parcelable.Creator<ByActivityCategory>() {
            public ByActivityCategory createFromParcel(Parcel source) {
                return new ByActivityCategory(source);
            }

            public ByActivityCategory[] newArray(int size) {
                return new ByActivityCategory[size];
            }
        };
    }

    /**
     * Messages, open reports and non default states count by asset id
     * Messages count includes report and states messages count
     */
    public static class ByAssetId implements Parcelable {
        /**
         * assetId <=> counts
         */
        public HashMap<String, Item> countsByAssetId = new HashMap<>();

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

        public ByAssetId() {
        }

        protected ByAssetId(Parcel in) {
            Bundle messagesCountsBundle = in.readBundle();
            for (String assetId : messagesCountsBundle.keySet()) {
                countsByAssetId.put(assetId, messagesCountsBundle.<Item>getParcelable(assetId));
            }
        }

        public static final Parcelable.Creator<ByAssetId> CREATOR = new Parcelable.Creator<ByAssetId>() {
            public ByAssetId createFromParcel(Parcel source) {
                return new ByAssetId(source);
            }

            public ByAssetId[] newArray(int size) {
                return new ByAssetId[size];
            }
        };
    }

    public static class Item implements Parcelable {
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

        public Item() {
        }

        protected Item(Parcel in) {
            this.messageCount = in.readInt();
            this.openReportsCount = in.readInt();
            this.nonDefaultStateCount = in.readInt();
        }

        public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
            public Item createFromParcel(Parcel source) {
                return new Item(source);
            }

            public Item[] newArray(int size) {
                return new Item[size];
            }
        };
    }
}
