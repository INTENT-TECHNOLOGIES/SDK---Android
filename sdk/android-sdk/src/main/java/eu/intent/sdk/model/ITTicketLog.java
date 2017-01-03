package eu.intent.sdk.model;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import eu.intent.sdk.util.ITJsonUtils;

/**
 * This is an event in a ticket.
 */

public class ITTicketLog {
    public boolean additionalOrder;
    @Nullable
    public String additionalOrderComment;
    @Nullable
    public String assetReference;
    @Nullable
    public ITAssetType assetType;
    @Nullable
    public String caseReference;
    @Nullable
    public String comment;
    @Nullable
    public String contractReference;
    @Nullable
    public String dischargeDocReference;
    @Nullable
    public String equipmentStatus;
    @Nullable
    public String equipmentWorkingOrder;
    @Nullable
    public String event;
    @Nullable
    public String externalReference;
    @Nullable
    public String fileKey;
    @Nullable
    public String interventionReference;
    @Nullable
    public String occupantSignature;
    @Nullable
    public String orderReference;
    @Nullable
    public String orderDocReference;
    @Nullable
    public String providerContacts;
    @Nullable
    public String replacingEquipment;
    @Nullable
    public String requestOrigin;
    @Nullable
    public String requestReference;
    @Nullable
    public String serviceCode;
    @Nullable
    public String status;
    @Nullable
    public String technicalReason;
    @Nullable
    public String tradeReference;
    public int visitAttempt;
    public boolean warning;
    @Nullable
    public String warningComment;

    transient public long creationDate;
    transient public long eventDate;

    public static class Deserializer implements JsonDeserializer<ITTicketLog> {
        @Override
        public ITTicketLog deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder().create();
            ITTicketLog log = gson.fromJson(json, typeOfT);
            JsonObject jsonObject = json.getAsJsonObject();
            log.creationDate = ITJsonUtils.parseJsonIsoDate(jsonObject, "creationDate");
            log.eventDate = ITJsonUtils.parseJsonIsoDate(jsonObject, "eventDate");
            return log;
        }
    }
}
