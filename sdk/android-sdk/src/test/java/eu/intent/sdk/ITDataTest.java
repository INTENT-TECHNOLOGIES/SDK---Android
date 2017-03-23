package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITData;
import eu.intent.sdk.model.ITDataSet;

import static junit.framework.Assert.assertEquals;

public class ITDataTest {
    @Test
    public void jsonToDataResultList() {
        String json = TestUtils.INSTANCE.getResourceAsString("data_sets.json");
        ITDataSet[] dataSets = TestUtils.INSTANCE.getGson().fromJson(json, ITDataSet[].class);
        assertEquals(1, dataSets.length);
        assertEquals(ITData.Type.SNAPSHOT, dataSets[0].type);
        assertEquals(3, dataSets[0].data.size());
        assertEquals(1458061814574L, dataSets[0].data.get(0).timestamp);
        assertEquals(1458661878227L, dataSets[0].data.get(1).timestamp);
        assertEquals(1460049538627L, dataSets[0].data.get(2).timestamp);
        assertEquals(789.0, dataSets[0].data.get(0).value);
        assertEquals(456.7, dataSets[0].data.get(1).value);
        assertEquals(Double.NaN, dataSets[0].data.get(2).value);
        assertEquals(Double.NaN, dataSets[0].data.get(0).valueMin);
        assertEquals(Double.NaN, dataSets[0].data.get(1).valueMin);
        assertEquals(12.3, dataSets[0].data.get(2).valueMin);
        assertEquals(Double.NaN, dataSets[0].data.get(0).valueMax);
        assertEquals(Double.NaN, dataSets[0].data.get(1).valueMax);
        assertEquals(23.4, dataSets[0].data.get(2).valueMax);
        assertEquals(ITData.TrustLevel.TRUSTED_MANUAL, dataSets[0].data.get(0).trustLevel);
        assertEquals(ITData.TrustLevel.TRUSTED_MANUAL, dataSets[0].data.get(1).trustLevel);
        assertEquals(ITData.TrustLevel.TRUSTED_SENSOR, dataSets[0].data.get(2).trustLevel);
    }
}
