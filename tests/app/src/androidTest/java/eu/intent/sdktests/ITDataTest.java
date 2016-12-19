package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITData;
import eu.intent.sdk.model.ITDataResult;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITDataTest {
    @Test
    public void jsonToDataResultList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.data_results);
        Type listType = new TypeToken<List<ITDataResult>>() {
        }.getType();
        List<ITDataResult> dataResults = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(1, dataResults.size());
        assertEquals(ITData.Type.SNAPSHOT, dataResults.get(0).type);
        assertEquals(3, dataResults.get(0).data.size());
        assertEquals(1458061814574L, dataResults.get(0).data.get(0).timestamp);
        assertEquals(1458661878227L, dataResults.get(0).data.get(1).timestamp);
        assertEquals(1460049538627L, dataResults.get(0).data.get(2).timestamp);
        assertEquals(789.0, dataResults.get(0).data.get(0).value);
        assertEquals(456.7, dataResults.get(0).data.get(1).value);
        assertEquals(Double.NaN, dataResults.get(0).data.get(2).value);
        assertEquals(Double.NaN, dataResults.get(0).data.get(0).valueMin);
        assertEquals(Double.NaN, dataResults.get(0).data.get(1).valueMin);
        assertEquals(12.3, dataResults.get(0).data.get(2).valueMin);
        assertEquals(Double.NaN, dataResults.get(0).data.get(0).valueMax);
        assertEquals(Double.NaN, dataResults.get(0).data.get(1).valueMax);
        assertEquals(23.4, dataResults.get(0).data.get(2).valueMax);
        assertEquals(ITData.TrustLevel.TRUSTED_MANUAL, dataResults.get(0).data.get(0).trustLevel);
        assertEquals(ITData.TrustLevel.TRUSTED_MANUAL, dataResults.get(0).data.get(1).trustLevel);
        assertEquals(ITData.TrustLevel.TRUSTED_SENSOR, dataResults.get(0).data.get(2).trustLevel);
    }
}
