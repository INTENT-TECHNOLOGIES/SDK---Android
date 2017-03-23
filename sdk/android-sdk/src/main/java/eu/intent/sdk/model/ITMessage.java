package eu.intent.sdk.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Message that take part of a {@link ITConversation}
 * /!\ some class members can be null depending of the ITMessage {@link eu.intent.sdk.model.ITMessage.MessageType}
 * <p/>
 * reportingState, reportingStateLastChange and reportingStateHistory are only set if type is ITMessage.MessageType.REPORTING, </br>
 * state is only set if type is ITMessage.MessageType.STATE
 */
public class ITMessage implements Parcelable {
    public static final Parcelable.Creator<ITMessage> CREATOR = new Parcelable.Creator<ITMessage>() {
        public ITMessage createFromParcel(Parcel source) {
            return new ITMessage(source);
        }

        public ITMessage[] newArray(int size) {
            return new ITMessage[size];
        }
    };

    public MessageType type;
    public String id;
    public String title;
    public String message;
    public boolean removed;
    public long creationDate;
    public long lastUpdate;
    public String creatorName;
    public String creatorDomain;

    //Reporting type only
    /**
     * Current reporting state, only present if type is REPORTING
     */
    public ReportingState reportingState;
    /**
     * History of reporting state, contains the current state and only present if type is REPORTING
     */
    public List<ReportingStateHistoryItem> reportingStateHistory;

    //State type only
    /**
     * State payload of the message, only present if type is STATE
     */
    public ITState state;

    public ITMessage() {
        // Needed by Gson
    }

    protected ITMessage(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : MessageType.values()[tmpType];
        this.id = in.readString();
        this.title = in.readString();
        this.message = in.readString();
        this.removed = in.readByte() != 0;
        this.creationDate = in.readLong();
        this.lastUpdate = in.readLong();
        this.creatorName = in.readString();
        this.creatorDomain = in.readString();
        int tmpReportingState = in.readInt();
        this.reportingState = tmpReportingState == -1 ? null : ReportingState.values()[tmpReportingState];
        this.reportingStateHistory = new ArrayList<>();
        in.readList(this.reportingStateHistory, List.class.getClassLoader());
        in.readParcelable(ITState.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.message);
        dest.writeByte(removed ? (byte) 1 : (byte) 0);
        dest.writeLong(this.creationDate);
        dest.writeLong(this.lastUpdate);
        dest.writeString(this.creatorName);
        dest.writeString(this.creatorDomain);
        dest.writeInt(this.reportingState == null ? -1 : this.reportingState.ordinal());
        dest.writeList(this.reportingStateHistory);
        dest.writeParcelable(this.state, flags);
    }

    public enum MessageType {
        @SerializedName("COMMENT")COMMENT,
        @SerializedName("REPORTING")REPORTING,
        @SerializedName("STATE")STATE
    }

    public enum ReportingState {
        @SerializedName("OPEN")OPEN,
        @SerializedName("CLOSED")CLOSED,
        @SerializedName("PROCESSING")PROCESSING
    }

    public static class ReportingStateHistoryItem {
        public ReportingState state;
        public long timestamp;
        @SerializedName("changer")
        public String changerUserId;
        public String changerName;
    }

    public static class Deserializer implements JsonDeserializer<ITMessage> {
        @Override
        public ITMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder().registerTypeAdapter(ITState.class, new ITState.Deserializer()).create();
            ITMessage message = gson.fromJson(json, typeOfT);

            //Payload is empty for COMMENT messages types
            if (MessageType.REPORTING.equals(message.type) || MessageType.STATE.equals(message.type)) {
                JsonObject payload = json.getAsJsonObject().getAsJsonObject("payload");
                if (payload != null) {
                    deserializePayload(message, payload, gson);
                }
            }

            return message;
        }

        private void deserializePayload(@NonNull ITMessage message, @NonNull JsonObject payload, @NonNull Gson gson) {
            if (MessageType.REPORTING.equals(message.type)) {
                message.reportingState = gson.fromJson(payload.get("state"), ReportingState.class);
                message.reportingStateHistory = gson.fromJson(payload.getAsJsonArray("stateHistory"),
                        new TypeToken<ArrayList<ReportingStateHistoryItem>>() {
                        }.getType());
            } else if (MessageType.STATE.equals(message.type)) {
                message.state = gson.fromJson(payload, new TypeToken<ITState>() {
                }.getType());
            }
        }
    }
}
