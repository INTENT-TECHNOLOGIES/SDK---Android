package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.api.ITTicketApi;
import eu.intent.sdk.model.ITAssetType;
import eu.intent.sdk.model.ITLinkedAssetType;
import eu.intent.sdk.model.ITTicket;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ITTicketTest {
    @Test
    public void emptySearchCriteriaToJson() {
        ITTicketApi.SearchCriteria searchCriteria = new ITTicketApi.SearchCriteria.Builder().create();
        assertNull(searchCriteria.assetReference);
        assertNull(searchCriteria.assetType);
        assertNull(searchCriteria.caseReference);
        assertNull(searchCriteria.contractReference);
        assertNull(searchCriteria.from);
        assertNull(searchCriteria.interventionReference);
        assertNull(searchCriteria.linkedAssets);
        assertNull(searchCriteria.status);
        assertNull(searchCriteria.to);
        assertNull(searchCriteria.tradeReference);
        assertNull(searchCriteria.updatedSince);
        assertNull(searchCriteria.withLogs);

        JsonElement jsonSearchCriteria = ITRetrofitUtils.getGson().toJsonTree(searchCriteria);
        assertTrue(jsonSearchCriteria.isJsonObject());
        assertTrue(jsonSearchCriteria.getAsJsonObject().entrySet().isEmpty());
    }

    @Test
    public void searchCriteriaToJson() {
        ITTicketApi.SearchCriteria searchCriteria = new ITTicketApi.SearchCriteria.Builder()
                .assetReference("assetReference")
                .assetType(ITAssetType.SITE)
                .caseReference("caseReference")
                .contractReference("contractReference")
                .from(123000)
                .interventionReference("interventionReference")
                .status("status")
                .to(456000)
                .tradeReference("tradeReference")
                .updatedSince(789000)
                .withLinkedAssets(ITLinkedAssetType.PARTS, ITLinkedAssetType.EQUIPMENTS)
                .withLogs(true)
                .create();
        assertEquals("assetReference", searchCriteria.assetReference);
        assertEquals(ITAssetType.SITE, searchCriteria.assetType);
        assertEquals("caseReference", searchCriteria.caseReference);
        assertEquals("contractReference", searchCriteria.contractReference);
        assertEquals("1970-01-01T00:02:03.000+0000", searchCriteria.from);
        assertEquals("interventionReference", searchCriteria.interventionReference);
        assertEquals(2, searchCriteria.linkedAssets.length);
        assertEquals("status", searchCriteria.status);
        assertEquals("1970-01-01T00:07:36.000+0000", searchCriteria.to);
        assertEquals("tradeReference", searchCriteria.tradeReference);
        assertEquals("1970-01-01T00:13:09.000+0000", searchCriteria.updatedSince);
        assertTrue(searchCriteria.withLogs);

        JsonElement jsonSearchCriteria = ITRetrofitUtils.getGson().toJsonTree(searchCriteria);
        assertEquals("assetReference", jsonSearchCriteria.getAsJsonObject().get("assetReference").getAsString());
        assertEquals("site", jsonSearchCriteria.getAsJsonObject().get("assetType").getAsString());
        assertEquals("caseReference", jsonSearchCriteria.getAsJsonObject().get("caseReference").getAsString());
        assertEquals("contractReference", jsonSearchCriteria.getAsJsonObject().get("contractReference").getAsString());
        assertEquals("1970-01-01T00:02:03.000+0000", jsonSearchCriteria.getAsJsonObject().get("from").getAsString());
        assertEquals("interventionReference", jsonSearchCriteria.getAsJsonObject().get("interventionReference").getAsString());
        assertEquals("status", jsonSearchCriteria.getAsJsonObject().get("status").getAsString());
        assertEquals("1970-01-01T00:07:36.000+0000", jsonSearchCriteria.getAsJsonObject().get("to").getAsString());
        assertEquals("tradeReference", jsonSearchCriteria.getAsJsonObject().get("tradeReference").getAsString());
        assertEquals("1970-01-01T00:13:09.000+0000", jsonSearchCriteria.getAsJsonObject().get("updatedSince").getAsString());
        assertEquals(2, jsonSearchCriteria.getAsJsonObject().get("linkedAssets").getAsJsonArray().size());
        assertTrue(jsonSearchCriteria.getAsJsonObject().get("linkedAssets").getAsJsonArray().contains(new JsonPrimitive("parts")));
        assertTrue(jsonSearchCriteria.getAsJsonObject().get("linkedAssets").getAsJsonArray().contains(new JsonPrimitive("equips")));
        assertTrue(jsonSearchCriteria.getAsJsonObject().get("withLogs").getAsBoolean());
    }

    @Test
    public void searchCriteriaWithAllLinkedAssetsToJson() {
        ITTicketApi.SearchCriteria searchCriteria = new ITTicketApi.SearchCriteria.Builder()
                .withLinkedAssets(ITLinkedAssetType.ALL)
                .create();
        assertEquals(1, searchCriteria.linkedAssets.length);
        assertEquals("all", searchCriteria.linkedAssets[0]);

        JsonElement jsonSearchCriteria = ITRetrofitUtils.getGson().toJsonTree(searchCriteria);
        assertTrue(jsonSearchCriteria.getAsJsonObject().get("linkedAssets").getAsJsonArray().contains(new JsonPrimitive("all")));
    }

    @Test
    public void jsonToTickets() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.tickets);
        Type listType = new TypeToken<List<ITTicket>>() {
        }.getType();
        List<ITTicket> tickets = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(1, tickets.size());
        assertEquals("TEST-001", tickets.get(0).assetReference);
        assertEquals(ITAssetType.EQUIPMENT, tickets.get(0).assetType);
        assertEquals("", tickets.get(0).caseReference);
        assertEquals("CONTRACT-001", tickets.get(0).contractReference);
        assertEquals("done", tickets.get(0).event);
        assertEquals("", tickets.get(0).externalReference);
        assertEquals("INT-001", tickets.get(0).interventionReference);
        assertEquals("test.intent", tickets.get(0).provider);
        assertEquals("", tickets.get(0).requestReference);
        assertEquals("closed", tickets.get(0).status);
        assertEquals("TRADE-001", tickets.get(0).tradeReference);
        assertEquals(1478246400000L, tickets.get(0).firstEventDate);
        assertEquals(1479142779191L, tickets.get(0).lastEventDate);
        assertEquals(1479142932679L, tickets.get(0).lastUpdate);
        assertNotNull(tickets.get(0).logs);
        assertEquals(6, tickets.get(0).logs.size());
        assertEquals(false, tickets.get(0).logs.get(0).additionalOrder);
        assertEquals(null, tickets.get(0).logs.get(0).additionalOrderComment);
        assertEquals("TEST-001", tickets.get(0).logs.get(0).assetReference);
        assertEquals(ITAssetType.EQUIPMENT, tickets.get(0).logs.get(0).assetType);
        assertEquals(null, tickets.get(0).logs.get(0).caseReference);
        assertEquals("eclairage cage d'escalier HS, RDC et 1er etage", tickets.get(0).logs.get(0).comment);
        assertEquals("CONTRACT-001", tickets.get(0).logs.get(0).contractReference);
        assertEquals(null, tickets.get(0).logs.get(0).dischargeDocReference);
        assertEquals(null, tickets.get(0).logs.get(0).equipmentStatus);
        assertEquals(null, tickets.get(0).logs.get(0).equipmentWorkingOrder);
        assertEquals("acknowledged", tickets.get(0).logs.get(0).event);
        assertEquals(null, tickets.get(0).logs.get(0).externalReference);
        assertEquals("file.key", tickets.get(0).logs.get(0).fileKey);
        assertEquals("INT-001", tickets.get(0).logs.get(0).interventionReference);
        assertEquals(null, tickets.get(0).logs.get(0).occupantSignature);
        assertEquals(null, tickets.get(0).logs.get(0).orderReference);
        assertEquals(null, tickets.get(0).logs.get(0).orderDocReference);
        assertEquals("prestataire@service.com", tickets.get(0).logs.get(0).providerContacts);
        assertEquals(null, tickets.get(0).logs.get(0).replacingEquipment);
        assertEquals("sp", tickets.get(0).logs.get(0).requestOrigin);
        assertEquals(null, tickets.get(0).logs.get(0).requestReference);
        assertEquals("curative-eclairage", tickets.get(0).logs.get(0).serviceCode);
        assertEquals("open", tickets.get(0).logs.get(0).status);
        assertEquals(null, tickets.get(0).logs.get(0).technicalReason);
        assertEquals("TRADE-001", tickets.get(0).logs.get(0).tradeReference);
        assertEquals(0, tickets.get(0).logs.get(0).visitAttempt);
        assertEquals(false, tickets.get(0).logs.get(0).warning);
        assertEquals(null, tickets.get(0).logs.get(0).warningComment);
        assertEquals(1479142779191L, tickets.get(0).logs.get(0).creationDate);
        assertEquals(1478246400000L, tickets.get(0).logs.get(0).eventDate);
    }
}
