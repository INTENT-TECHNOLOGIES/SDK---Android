package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITMessagesCount;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITMessageTest {
    @Test
    public void jsonToMessageCountByActivityCategory() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.message_count_by_activity_category);
        ITMessagesCount.ByActivityCategory messageCount = ITRetrofitUtils.getGson().fromJson(json, ITMessagesCount.ByActivityCategory.class);
        assertEquals(2, messageCount.countsByActivityCategory.size());
        assertEquals(1, messageCount.countsByActivityCategory.get("HEATING").messageCount);
        assertEquals(0, messageCount.countsByActivityCategory.get("WATER").messageCount);
        assertEquals(2, messageCount.countsByActivityCategory.get("HEATING").openReportsCount);
        assertEquals(0, messageCount.countsByActivityCategory.get("WATER").openReportsCount);
        assertEquals(3, messageCount.countsByActivityCategory.get("HEATING").nonDefaultStateCount);
        assertEquals(0, messageCount.countsByActivityCategory.get("WATER").nonDefaultStateCount);
    }

    @Test
    public void jsonToMessageCountByAsset() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.message_count_by_asset);
        ITMessagesCount.ByAssetId messageCount = ITRetrofitUtils.getGson().fromJson(json, ITMessagesCount.ByAssetId.class);
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
