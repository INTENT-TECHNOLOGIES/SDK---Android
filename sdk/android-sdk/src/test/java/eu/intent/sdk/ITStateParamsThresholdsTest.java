package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITStateParamsThresholds;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ITStateParamsThresholdsTest {
    @Test
    public void jsonToStateParamsThresholdsList() {
        String json = TestUtils.INSTANCE.getResourceAsString("state_thresholds.json");
        ITStateParamsThresholds[] stateThresholdList = TestUtils.INSTANCE.getGson().fromJson(json, ITStateParamsThresholds[].class);
        assertEquals(1, stateThresholdList[0].thresholds.length);
        assertEquals(-1, stateThresholdList[0].thresholds[0].dayOfWeek);
        assertEquals(-1, stateThresholdList[0].thresholds[0].fromHour);
        assertEquals(0, stateThresholdList[0].thresholds[0].fromMinute);
        assertEquals(-1, stateThresholdList[0].thresholds[0].toHour);
        assertEquals(0, stateThresholdList[0].thresholds[0].toMinute);
        assertEquals(2.5, stateThresholdList[0].thresholds[0].max);
        assertTrue(Double.isNaN(stateThresholdList[0].thresholds[0].min));
        assertTrue(stateThresholdList[0].thresholds[0].isInEffectAt(System.currentTimeMillis()));
        assertEquals(2, stateThresholdList[1].thresholds.length);
        assertEquals(-1, stateThresholdList[1].thresholds[0].dayOfWeek);
        assertEquals(8, stateThresholdList[1].thresholds[0].fromHour);
        assertEquals(30, stateThresholdList[1].thresholds[0].fromMinute);
        assertEquals(18, stateThresholdList[1].thresholds[0].toHour);
        assertEquals(30, stateThresholdList[1].thresholds[0].toMinute);
        assertFalse(stateThresholdList[1].thresholds[0].isAcrossTwoDays());
        assertEquals(-1, stateThresholdList[1].thresholds[1].dayOfWeek);
        assertEquals(18, stateThresholdList[1].thresholds[1].fromHour);
        assertEquals(30, stateThresholdList[1].thresholds[1].fromMinute);
        assertEquals(8, stateThresholdList[1].thresholds[1].toHour);
        assertEquals(30, stateThresholdList[1].thresholds[1].toMinute);
        assertTrue(stateThresholdList[1].thresholds[1].isAcrossTwoDays());
        long april26thMidNight = 1461628800000L;
        long april26thMidDay = 1461672000000L;
        assertFalse(stateThresholdList[1].thresholds[0].isInEffectAt(april26thMidNight));
        assertTrue(stateThresholdList[1].thresholds[0].isInEffectAt(april26thMidDay));
        assertTrue(stateThresholdList[1].thresholds[1].isInEffectAt(april26thMidNight));
        assertFalse(stateThresholdList[1].thresholds[1].isInEffectAt(april26thMidDay));
        assertEquals(1, stateThresholdList[2].thresholds.length);
        assertEquals(0, stateThresholdList[2].thresholds[0].dayOfWeek); // 0 = sunday
        assertEquals(10, stateThresholdList[2].thresholds[0].fromHour);
        assertEquals(0, stateThresholdList[2].thresholds[0].fromMinute);
        assertEquals(20, stateThresholdList[2].thresholds[0].toHour);
        assertEquals(0, stateThresholdList[2].thresholds[0].toMinute);
        long sundayMay1st6oclock = 1462082400000L;
        long sundayMay1st14oclock = 1462111200000L;
        long mondayMay2nd6oclock = 1462168800000L;
        long mondayMay2nd14oclock = 1462197600000L;
        assertFalse(stateThresholdList[2].thresholds[0].isInEffectAt(sundayMay1st6oclock));
        assertTrue(stateThresholdList[2].thresholds[0].isInEffectAt(sundayMay1st14oclock));
        assertFalse(stateThresholdList[2].thresholds[0].isInEffectAt(mondayMay2nd6oclock));
        assertFalse(stateThresholdList[2].thresholds[0].isInEffectAt(mondayMay2nd14oclock));
    }
}
