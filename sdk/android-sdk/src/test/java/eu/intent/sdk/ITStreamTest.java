package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITData;
import eu.intent.sdk.model.ITStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class ITStreamTest {
    @Test
    public void jsonToStreams() {
        String json = TestUtils.INSTANCE.getResourceAsString("stream.json");
        ITStream stream = TestUtils.INSTANCE.getGson().fromJson(json, ITStream.class);
        assertEquals(1461757500884L, stream.lastUpdate);
        assertEquals("c1fe60e5-7f1e-4272-bd55-7b61adf9ecc8", stream.id);
        assertEquals("demo1", stream.owner);
        assertNotNull(stream.lastValue);
        assertEquals(1461754800000L, stream.lastValue.timestamp);
        assertEquals(ITData.TrustLevel.COMPUTED, stream.lastValue.trustLevel);
        assertEquals(22.416, stream.lastValue.value);
        assertTrue(Double.isNaN(stream.lastValue.valueMin));
        assertTrue(Double.isNaN(stream.lastValue.valueMax));
        assertEquals(7, stream.tags.size());
        assertEquals("airTempAgg", stream.tags.get("measureType"));
        assertEquals("celcius", stream.tags.get("unit"));
        assertEquals("inside", stream.tags.get("position"));
        assertEquals("AggSiteTemp", stream.tags.get("activityKey"));
        assertEquals("SITE-ROMAGNAT-000538", stream.tags.get("siteId"));
        assertEquals("temp", stream.tags.get("icon"));
        assertEquals("Site-Démo2", stream.tags.get("siteExternalRef"));
    }
}
