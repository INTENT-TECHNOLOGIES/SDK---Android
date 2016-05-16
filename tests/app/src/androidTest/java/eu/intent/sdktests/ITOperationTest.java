package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITOperationList;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITOperationTest {
    @Test
    public void jsonToOperationList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.operations);
        ITOperationList operationList = ITRetrofitUtils.getGson().fromJson(json, ITOperationList.class);
        assertEquals(2, operationList.totalCount);
        assertEquals(2, operationList.operations.size());
        assertEquals(true, operationList.operations.get(0).active);
        assertEquals(false, operationList.operations.get(1).active);
        assertEquals(true, operationList.operations.get(0).published);
        assertEquals(false, operationList.operations.get(1).published);
        assertEquals("lmh.intent", operationList.operations.get(0).client);
        assertEquals("partenord.intent", operationList.operations.get(1).client);
        assertEquals("intentstaff.intent", operationList.operations.get(0).contractor);
        assertEquals("intentstaff.intent", operationList.operations.get(1).contractor);
        assertEquals("54919c61ed1226ff707246c5", operationList.operations.get(0).id);
        assertEquals("5497cb9d02c9bbc8298abc98", operationList.operations.get(1).id);
        assertEquals("ARHLM LMH", operationList.operations.get(0).name);
        assertEquals("ARHLM PARTENORD site ANZIN", operationList.operations.get(1).name);
        assertEquals(1427449271669L, operationList.operations.get(0).lastUpdate);
        assertEquals(1419872983010L, operationList.operations.get(1).lastUpdate);
    }
}
