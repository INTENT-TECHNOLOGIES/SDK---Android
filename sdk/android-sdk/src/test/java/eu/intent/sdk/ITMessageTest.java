package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITMessagesCount;

import static junit.framework.Assert.assertEquals;

public class ITMessageTest {
    @Test
    public void jsonToMessageCountByActivityCategory() {
        String json = TestUtils.INSTANCE.getResourceAsString("message_count_by_activity_category.json");
        ITMessagesCount.ByTheme messageCount = TestUtils.INSTANCE.getGson().fromJson(json, ITMessagesCount.ByTheme.class);
        assertEquals(2, messageCount.countsByTheme.size());
        assertEquals(1, messageCount.countsByTheme.get("HEATING").messageCount);
        assertEquals(0, messageCount.countsByTheme.get("WATER").messageCount);
        assertEquals(2, messageCount.countsByTheme.get("HEATING").openReportsCount);
        assertEquals(0, messageCount.countsByTheme.get("WATER").openReportsCount);
        assertEquals(3, messageCount.countsByTheme.get("HEATING").nonDefaultStateCount);
        assertEquals(0, messageCount.countsByTheme.get("WATER").nonDefaultStateCount);
    }

    @Test
    public void jsonToMessageCountByAsset() {
        String json = TestUtils.INSTANCE.getResourceAsString("message_count_by_asset.json");
        ITMessagesCount.ByAssetId messageCount = TestUtils.INSTANCE.getGson().fromJson(json, ITMessagesCount.ByAssetId.class);
        assertEquals(5, messageCount.countsByAssetId.size());
        assertEquals(0, messageCount.countsByAssetId.get("SITE-1").messageCount);
        assertEquals(103, messageCount.countsByAssetId.get("SITE-2").messageCount);
        assertEquals(66, messageCount.countsByAssetId.get("SITE-3").messageCount);
        assertEquals(68, messageCount.countsByAssetId.get("SITE-4").messageCount);
        assertEquals(22, messageCount.countsByAssetId.get("SITE-5").messageCount);
        assertEquals(0, messageCount.countsByAssetId.get("SITE-1").openReportsCount);
        assertEquals(0, messageCount.countsByAssetId.get("SITE-2").openReportsCount);
        assertEquals(0, messageCount.countsByAssetId.get("SITE-3").openReportsCount);
        assertEquals(34, messageCount.countsByAssetId.get("SITE-4").openReportsCount);
        assertEquals(12, messageCount.countsByAssetId.get("SITE-5").openReportsCount);
        assertEquals(0, messageCount.countsByAssetId.get("SITE-1").nonDefaultStateCount);
        assertEquals(0, messageCount.countsByAssetId.get("SITE-2").nonDefaultStateCount);
        assertEquals(1, messageCount.countsByAssetId.get("SITE-3").nonDefaultStateCount);
        assertEquals(0, messageCount.countsByAssetId.get("SITE-4").nonDefaultStateCount);
        assertEquals(3, messageCount.countsByAssetId.get("SITE-5").nonDefaultStateCount);
    }
}
