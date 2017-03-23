package eu.intent.sdk;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import eu.intent.sdk.api.ITOperationApi;
import eu.intent.sdk.model.ITAssetType;
import eu.intent.sdk.model.ITLinkedAssetType;
import eu.intent.sdk.model.ITTicket;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ITTicketTest {
    @Test
    public void emptySearchCriteriaToJson() {
        ITOperationApi.SearchCriteria searchCriteria = new ITOperationApi.SearchCriteria.Builder().create();
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

        JsonElement jsonSearchCriteria = TestUtils.INSTANCE.getGson().toJsonTree(searchCriteria);
        assertTrue(jsonSearchCriteria.isJsonObject());
        assertTrue(jsonSearchCriteria.getAsJsonObject().entrySet().isEmpty());
    }

    @Test
    public void searchCriteriaToJson() {
        ITOperationApi.SearchCriteria searchCriteria = new ITOperationApi.SearchCriteria.Builder()
                .assetReference("assetReference")
                .assetType(ITAssetType.SITE)
                .caseReference("caseReference")
                .contractReference("contractReference")
                .fromDate(123000)
                .interventionReference("interventionReference")
                .status("status")
                .toDate(456000)
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

        JsonElement jsonSearchCriteria = TestUtils.INSTANCE.getGson().toJsonTree(searchCriteria);
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
        ITOperationApi.SearchCriteria searchCriteria = new ITOperationApi.SearchCriteria.Builder()
                .withLinkedAssets(ITLinkedAssetType.ALL)
                .create();
        assertEquals(1, searchCriteria.linkedAssets.length);
        assertEquals("all", searchCriteria.linkedAssets[0]);

        JsonElement jsonSearchCriteria = TestUtils.INSTANCE.getGson().toJsonTree(searchCriteria);
        assertTrue(jsonSearchCriteria.getAsJsonObject().get("linkedAssets").getAsJsonArray().contains(new JsonPrimitive("all")));
    }

    @Test
    public void jsonToTickets() {
        String json = TestUtils.INSTANCE.getResourceAsString("tickets.json");
        ITTicket[] tickets = TestUtils.INSTANCE.getGson().fromJson(json, ITTicket[].class);
        assertEquals(1, tickets.length);
        assertEquals("TEST-001", tickets[0].assetReference);
        assertEquals(ITAssetType.EQUIPMENT, tickets[0].assetType);
        assertEquals("", tickets[0].caseReference);
        assertEquals("CONTRACT-001", tickets[0].contractReference);
        assertEquals("done", tickets[0].event);
        assertEquals("", tickets[0].externalReference);
        assertEquals("INT-001", tickets[0].interventionReference);
        assertEquals("test.intent", tickets[0].provider);
        assertEquals("", tickets[0].requestReference);
        assertEquals("closed", tickets[0].status);
        assertEquals("TRADE-001", tickets[0].tradeReference);
        assertEquals(1478246400000L, tickets[0].firstEventDate);
        assertEquals(1479142779191L, tickets[0].lastEventDate);
        assertEquals(1479142932679L, tickets[0].lastUpdate);
        assertNotNull(tickets[0].logs);
        assertEquals(6, tickets[0].logs.size());
        assertEquals(false, tickets[0].logs.get(0).additionalOrder);
        assertEquals(null, tickets[0].logs.get(0).additionalOrderComment);
        assertEquals("TEST-001", tickets[0].logs.get(0).assetReference);
        assertEquals(ITAssetType.EQUIPMENT, tickets[0].logs.get(0).assetType);
        assertEquals(null, tickets[0].logs.get(0).caseReference);
        assertEquals("eclairage cage d'escalier HS, RDC et 1er etage", tickets[0].logs.get(0).comment);
        assertEquals("CONTRACT-001", tickets[0].logs.get(0).contractReference);
        assertEquals(null, tickets[0].logs.get(0).dischargeDocReference);
        assertEquals(null, tickets[0].logs.get(0).equipmentStatus);
        assertEquals(null, tickets[0].logs.get(0).equipmentWorkingOrder);
        assertEquals("acknowledged", tickets[0].logs.get(0).event);
        assertEquals(null, tickets[0].logs.get(0).externalReference);
        assertEquals("file.key", tickets[0].logs.get(0).fileKey);
        assertEquals("INT-001", tickets[0].logs.get(0).interventionReference);
        assertEquals(null, tickets[0].logs.get(0).occupantSignature);
        assertEquals(null, tickets[0].logs.get(0).orderReference);
        assertEquals(null, tickets[0].logs.get(0).orderDocReference);
        assertEquals("prestataire@service.com", tickets[0].logs.get(0).providerContacts);
        assertEquals(null, tickets[0].logs.get(0).replacingEquipment);
        assertEquals("sp", tickets[0].logs.get(0).requestOrigin);
        assertEquals(null, tickets[0].logs.get(0).requestReference);
        assertEquals("curative-eclairage", tickets[0].logs.get(0).serviceCode);
        assertEquals("open", tickets[0].logs.get(0).status);
        assertEquals(null, tickets[0].logs.get(0).technicalReason);
        assertEquals("TRADE-001", tickets[0].logs.get(0).tradeReference);
        assertEquals(0, tickets[0].logs.get(0).visitAttempt);
        assertEquals(false, tickets[0].logs.get(0).warning);
        assertEquals(null, tickets[0].logs.get(0).warningComment);
        assertEquals(1479142779191L, tickets[0].logs.get(0).creationDate);
        assertEquals(1478246400000L, tickets[0].logs.get(0).eventDate);
    }
}
