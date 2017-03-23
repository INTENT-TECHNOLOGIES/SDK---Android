package eu.intent.sdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import eu.intent.sdk.model.ITState;
import eu.intent.sdk.model.ITStateParamsThresholds;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ITStateTest {
    @Test
    public void jsonToSiteList() {
        String json = TestUtils.INSTANCE.getResourceAsString("states.json");
        ITState[] stateList = TestUtils.INSTANCE.getGson().fromJson(json, ITState[].class);
        assertEquals(5, stateList.length);
        assertEquals("demo1.intent", stateList[0].domain);
        assertEquals("0990d6aa-efe3-45cb-ae7f-7aca09ad31d0", stateList[0].observerId);
        assertEquals("069fee0b-f1c5-4a52-8515-4c0aebc8607d", stateList[2].observerId);
        assertEquals("ANORMAL", stateList[0].status);
        assertEquals("NORMAL", stateList[1].status);
        assertEquals("CRITICAL", stateList[2].status);
        assertEquals("ANORMAL", stateList[3].status);
        assertEquals("NORMAL", stateList[4].status);
        assertEquals("#ff0000", stateList[0].statusColor);
        assertEquals("NORMAL", stateList[0].statusDefault);
        assertEquals("Disparity-SiteTempVar-Max", stateList[0].templateId);
        assertEquals(1461674701179L, stateList[0].creationTime);
        assertEquals(0L, stateList[0].lastChange);
        assertEquals(0L, stateList[0].lastUpdate);
        assertEquals(1461625200000L, stateList[0].time);
        assertEquals(86400000L, stateList[0].validityDuration);
        assertEquals(1461711600000L, stateList[0].validityExpirationDate);
        assertEquals(false, stateList[0].isInDefaultStatus());
        assertEquals(true, stateList[1].isInDefaultStatus());
        assertEquals(false, stateList[2].isInDefaultStatus());
        assertEquals(false, stateList[3].isInDefaultStatus());
        assertEquals(true, stateList[4].isInDefaultStatus());
        assertEquals(3, stateList[0].statusEnum.length);
        assertEquals("NORMAL", stateList[0].statusEnum[0]);
        assertEquals("ANORMAL", stateList[0].statusEnum[1]);
        assertEquals("CRITICAL", stateList[0].statusEnum[2]);
        assertEquals("96d61d52-59e6-4637-99bb-8cd022bd5ccf", stateList[0].stream.id);
        assertEquals(5, stateList[0].texts.size());
        assertNotNull(stateList[0].texts.get("templateLabel"));
        assertNotNull(stateList[0].texts.get("label"));
        assertNotNull(stateList[0].texts.get("desc"));
        assertNotNull(stateList[0].texts.get("stateLabel"));
        assertNotNull(stateList[0].texts.get("stateDesc"));
        assertTrue(stateList[0].params instanceof ITStateParamsThresholds);
    }

    @Test
    public void testIsValid() {
        ITState state = new ITState();
        assertEquals(0, state.validityExpirationDate);
        assertTrue(state.isValid()); // If a state does not have validityExpirationDate property, assume it is still valid
        state.validityExpirationDate = System.currentTimeMillis() - 60000;
        assertFalse(state.isValid());
        state.validityExpirationDate = System.currentTimeMillis() + 60000;
        assertTrue(state.isValid());
        state.validityExpirationDate = System.currentTimeMillis();
        assertFalse(state.isValid()); // If the validityExpirationDate is just now, assume it is not valid anymore
    }
}
