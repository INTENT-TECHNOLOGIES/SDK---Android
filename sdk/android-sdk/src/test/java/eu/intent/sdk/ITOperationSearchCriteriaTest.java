package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.api.ITOperationApi;
import eu.intent.sdk.model.ITAssetType;
import eu.intent.sdk.model.ITLinkedAssetType;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class ITOperationSearchCriteriaTest {
    @Test
    public void emptySearchCriteria() {
        ITOperationApi.SearchCriteria criteria = new ITOperationApi.SearchCriteria.Builder().create();
        assertNull("Asset reference should be null", criteria.assetReference);
        assertNull("Asset type should be null", criteria.assetType);
        assertNull("Case reference should be null", criteria.caseReference);
        assertNull("Contract reference should be null", criteria.contractReference);
        assertNull("From date should be null", criteria.from);
        assertNull("Intervention reference should be null", criteria.interventionReference);
        assertNull("Status should be null", criteria.status);
        assertNull("To date should be null", criteria.to);
        assertNull("Trade reference should be null", criteria.tradeReference);
        assertNull("Updated since should be null", criteria.updatedSince);
        assertNull("Linked assets should be null", criteria.linkedAssets);
        assertNull("With logs should be null", criteria.withLogs);
    }

    @Test
    public void searchCriteriaWithLinkedAssets() {
        ITOperationApi.SearchCriteria criteria = new ITOperationApi.SearchCriteria.Builder()
                .assetReference("assetRef")
                .assetType(ITAssetType.EQUIPMENT)
                .caseReference("caseRef")
                .contractReference("contractRef")
                .fromDate(123L)
                .interventionReference("interventionRef")
                .status("status")
                .toDate(456L)
                .tradeReference("tradeRef")
                .updatedSince(789L)
                .withLinkedAssets(ITLinkedAssetType.ALL, ITLinkedAssetType.EQUIPMENTS, ITLinkedAssetType.PART_EQUIPMENTS, ITLinkedAssetType.PARTS, ITLinkedAssetType.SITES)
                .withLogs(true)
                .create();
        assertEquals("Asset reference should be equal to the parameter value", "assetRef", criteria.assetReference);
        assertEquals("Asset type should be equal to the parameter value", ITAssetType.EQUIPMENT, criteria.assetType);
        assertEquals("Case reference should be equal to the parameter value", "caseRef", criteria.caseReference);
        assertEquals("Contract reference should be equal to the parameter value", "contractRef", criteria.contractReference);
        assertEquals("From date should be equal to the parameter value", "1970-01-01T00:00:00.123+0000", criteria.from);
        assertEquals("Intervention reference should be equal to the parameter value", "interventionRef", criteria.interventionReference);
        assertEquals("Status should be equal to the parameter value", "status", criteria.status);
        assertEquals("To date should be equal to the parameter value", "1970-01-01T00:00:00.456+0000", criteria.to);
        assertEquals("Trade reference should be equal to the parameter value", "tradeRef", criteria.tradeReference);
        assertEquals("Updated since should be equal to the parameter value", "1970-01-01T00:00:00.789+0000", criteria.updatedSince);
        assertEquals("Linked assets should be equal to the parameter value", 5, criteria.linkedAssets.length);
        assertEquals("Linked assets should be equal to the parameter value", "all", criteria.linkedAssets[0]);
        assertEquals("Linked assets should be equal to the parameter value", "equips", criteria.linkedAssets[1]);
        assertEquals("Linked assets should be equal to the parameter value", "partsEquips", criteria.linkedAssets[2]);
        assertEquals("Linked assets should be equal to the parameter value", "parts", criteria.linkedAssets[3]);
        assertEquals("Linked assets should be equal to the parameter value", "sites", criteria.linkedAssets[4]);
        assertTrue("With logs should be equal to the parameter value", criteria.withLogs);
    }

    @Test
    public void searchCriteriaWithEmptyLinkedAssets() {
        ITOperationApi.SearchCriteria criteria = new ITOperationApi.SearchCriteria.Builder()
                .assetReference("assetRef")
                .assetType(ITAssetType.EQUIPMENT)
                .caseReference("caseRef")
                .contractReference("contractRef")
                .fromDate(123L)
                .interventionReference("interventionRef")
                .status("status")
                .toDate(456L)
                .tradeReference("tradeRef")
                .updatedSince(789L)
                .withLinkedAssets()
                .withLogs(true)
                .create();
        assertEquals("Asset reference should be equal to the parameter value", "assetRef", criteria.assetReference);
        assertEquals("Asset type should be equal to the parameter value", ITAssetType.EQUIPMENT, criteria.assetType);
        assertEquals("Case reference should be equal to the parameter value", "caseRef", criteria.caseReference);
        assertEquals("Contract reference should be equal to the parameter value", "contractRef", criteria.contractReference);
        assertEquals("From date should be equal to the parameter value", "1970-01-01T00:00:00.123+0000", criteria.from);
        assertEquals("Intervention reference should be equal to the parameter value", "interventionRef", criteria.interventionReference);
        assertEquals("Status should be equal to the parameter value", "status", criteria.status);
        assertEquals("To date should be equal to the parameter value", "1970-01-01T00:00:00.456+0000", criteria.to);
        assertEquals("Trade reference should be equal to the parameter value", "tradeRef", criteria.tradeReference);
        assertEquals("Updated since should be equal to the parameter value", "1970-01-01T00:00:00.789+0000", criteria.updatedSince);
        assertEquals("Linked assets should be empty", 0, criteria.linkedAssets.length);
        assertTrue("With logs should be equal to the parameter value", criteria.withLogs);
    }

    @Test
    public void searchCriteriaWithNullLinkedAssets() {
        ITOperationApi.SearchCriteria criteria = new ITOperationApi.SearchCriteria.Builder()
                .assetReference("assetRef")
                .assetType(ITAssetType.EQUIPMENT)
                .caseReference("caseRef")
                .contractReference("contractRef")
                .fromDate(123L)
                .interventionReference("interventionRef")
                .status("status")
                .toDate(456L)
                .tradeReference("tradeRef")
                .updatedSince(789L)
                .withLinkedAssets((ITLinkedAssetType[]) null)
                .withLogs(true)
                .create();
        assertEquals("Asset reference should be equal to the parameter value", "assetRef", criteria.assetReference);
        assertEquals("Asset type should be equal to the parameter value", ITAssetType.EQUIPMENT, criteria.assetType);
        assertEquals("Case reference should be equal to the parameter value", "caseRef", criteria.caseReference);
        assertEquals("Contract reference should be equal to the parameter value", "contractRef", criteria.contractReference);
        assertEquals("From date should be equal to the parameter value", "1970-01-01T00:00:00.123+0000", criteria.from);
        assertEquals("Intervention reference should be equal to the parameter value", "interventionRef", criteria.interventionReference);
        assertEquals("Status should be equal to the parameter value", "status", criteria.status);
        assertEquals("To date should be equal to the parameter value", "1970-01-01T00:00:00.456+0000", criteria.to);
        assertEquals("Trade reference should be equal to the parameter value", "tradeRef", criteria.tradeReference);
        assertEquals("Updated since should be equal to the parameter value", "1970-01-01T00:00:00.789+0000", criteria.updatedSince);
        assertNull("Linked assets should be null", criteria.linkedAssets);
        assertTrue("With logs should be equal to the parameter value", criteria.withLogs);
    }
}
