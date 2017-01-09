package eu.intent.sdk.model;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.List;

import eu.intent.sdk.util.ITJsonUtils;

/**
 * A ticket represents an intervention on an asset. It may have several logs representing its steps and status changes.
 */

public class ITTicket {
    @Nullable
    public String assetReference;
    @Nullable
    public ITAssetType assetType;
    @Nullable
    public String caseReference;
    @Nullable
    public String contractReference;
    @Nullable
    public String event;
    @Nullable
    public String externalReference;
    @Nullable
    public String interventionReference;
    @Nullable
    public List<ITTicketLog> logs;
    @Nullable
    public String provider;
    @Nullable
    public String requestReference;
    @Nullable
    public String status;
    @Nullable
    public String tradeReference;

    transient public long firstEventDate;
    transient public long lastEventDate;
    transient public long lastUpdate;

    public static class Deserializer implements JsonDeserializer<ITTicket> {
        @Override
        public ITTicket deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Gson gson = new GsonBuilder().registerTypeAdapter(ITTicketLog.class, new ITTicketLog.Deserializer()).create();
            ITTicket ticket = gson.fromJson(json, typeOfT);
            JsonObject jsonObject = json.getAsJsonObject();
            ticket.firstEventDate = ITJsonUtils.parseJsonIsoDate(jsonObject, "firstEventDate");
            ticket.lastEventDate = ITJsonUtils.parseJsonIsoDate(jsonObject, "lastEventDate");
            ticket.lastUpdate = ITJsonUtils.parseJsonIsoDate(jsonObject, "lastUpdate");
            return ticket;
        }
    }
}
