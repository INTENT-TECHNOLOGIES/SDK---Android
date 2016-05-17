package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITState;
import eu.intent.sdk.model.ITStateParamsThresholds;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ITStateTest {
    @Test
    public void jsonToSiteList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.states);
        Type listType = new TypeToken<List<ITState>>() {
        }.getType();
        List<ITState> stateList = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(5, stateList.size());
        assertEquals("demo1.intent", stateList.get(0).domain);
        assertEquals("0990d6aa-efe3-45cb-ae7f-7aca09ad31d0", stateList.get(0).observerId);
        assertEquals("069fee0b-f1c5-4a52-8515-4c0aebc8607d", stateList.get(2).observerId);
        assertEquals("ANORMAL", stateList.get(0).status);
        assertEquals("NORMAL", stateList.get(1).status);
        assertEquals("CRITICAL", stateList.get(2).status);
        assertEquals("ANORMAL", stateList.get(3).status);
        assertEquals("NORMAL", stateList.get(4).status);
        assertEquals("#ff0000", stateList.get(0).statusColor);
        assertEquals("NORMAL", stateList.get(0).statusDefault);
        assertEquals("Disparity-SiteTempVar-Max", stateList.get(0).templateId);
        assertEquals(1461674701179L, stateList.get(0).creationTime);
        assertEquals(0L, stateList.get(0).lastChange);
        assertEquals(0L, stateList.get(0).lastUpdate);
        assertEquals(1461625200000L, stateList.get(0).time);
        assertEquals(86400000L, stateList.get(0).validityDuration);
        assertEquals(1461711600000L, stateList.get(0).validityExpirationDate);
        assertFalse(stateList.get(0).isValid());
        assertEquals(false, stateList.get(0).isInDefaultStatus());
        assertEquals(true, stateList.get(1).isInDefaultStatus());
        assertEquals(false, stateList.get(2).isInDefaultStatus());
        assertEquals(false, stateList.get(3).isInDefaultStatus());
        assertEquals(true, stateList.get(4).isInDefaultStatus());
        assertEquals(3, stateList.get(0).statusEnum.length);
        assertEquals("NORMAL", stateList.get(0).statusEnum[0]);
        assertEquals("ANORMAL", stateList.get(0).statusEnum[1]);
        assertEquals("CRITICAL", stateList.get(0).statusEnum[2]);
        assertEquals("96d61d52-59e6-4637-99bb-8cd022bd5ccf", stateList.get(0).stream.id);
        assertEquals(5, stateList.get(0).texts.size());
        assertNotNull(stateList.get(0).texts.get("templateLabel"));
        assertNotNull(stateList.get(0).texts.get("label"));
        assertNotNull(stateList.get(0).texts.get("desc"));
        assertNotNull(stateList.get(0).texts.get("stateLabel"));
        assertNotNull(stateList.get(0).texts.get("stateDesc"));
        assertTrue(stateList.get(0).params instanceof ITStateParamsThresholds);
    }
}
