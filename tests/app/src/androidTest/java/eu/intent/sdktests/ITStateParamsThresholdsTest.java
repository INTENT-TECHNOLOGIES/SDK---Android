package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITStateParamsThresholds;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ITStateParamsThresholdsTest {
    @Test
    public void jsonToStateParamsThresholdsList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.state_thresholds);
        Type listType = new TypeToken<List<ITStateParamsThresholds>>() {
        }.getType();
        List<ITStateParamsThresholds> stateThresholdList = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(1, stateThresholdList.get(0).thresholds.length);
        assertEquals(-1, stateThresholdList.get(0).thresholds[0].dayOfWeek);
        assertEquals(-1, stateThresholdList.get(0).thresholds[0].fromHour);
        assertEquals(0, stateThresholdList.get(0).thresholds[0].fromMinute);
        assertEquals(-1, stateThresholdList.get(0).thresholds[0].toHour);
        assertEquals(0, stateThresholdList.get(0).thresholds[0].toMinute);
        assertEquals(2.5, stateThresholdList.get(0).thresholds[0].max);
        assertTrue(Double.isNaN(stateThresholdList.get(0).thresholds[0].min));
        assertTrue(stateThresholdList.get(0).thresholds[0].isInEffectAt(System.currentTimeMillis()));
        assertEquals(2, stateThresholdList.get(1).thresholds.length);
        assertEquals(-1, stateThresholdList.get(1).thresholds[0].dayOfWeek);
        assertEquals(8, stateThresholdList.get(1).thresholds[0].fromHour);
        assertEquals(30, stateThresholdList.get(1).thresholds[0].fromMinute);
        assertEquals(18, stateThresholdList.get(1).thresholds[0].toHour);
        assertEquals(30, stateThresholdList.get(1).thresholds[0].toMinute);
        assertFalse(stateThresholdList.get(1).thresholds[0].isAcrossTwoDays());
        assertEquals(-1, stateThresholdList.get(1).thresholds[1].dayOfWeek);
        assertEquals(18, stateThresholdList.get(1).thresholds[1].fromHour);
        assertEquals(30, stateThresholdList.get(1).thresholds[1].fromMinute);
        assertEquals(8, stateThresholdList.get(1).thresholds[1].toHour);
        assertEquals(30, stateThresholdList.get(1).thresholds[1].toMinute);
        assertTrue(stateThresholdList.get(1).thresholds[1].isAcrossTwoDays());
        long april26thMidNight = 1461628800000L;
        long april26thMidDay = 1461672000000L;
        assertFalse(stateThresholdList.get(1).thresholds[0].isInEffectAt(april26thMidNight));
        assertTrue(stateThresholdList.get(1).thresholds[0].isInEffectAt(april26thMidDay));
        assertTrue(stateThresholdList.get(1).thresholds[1].isInEffectAt(april26thMidNight));
        assertFalse(stateThresholdList.get(1).thresholds[1].isInEffectAt(april26thMidDay));
        assertEquals(1, stateThresholdList.get(2).thresholds.length);
        assertEquals(0, stateThresholdList.get(2).thresholds[0].dayOfWeek); // 0 = sunday
        assertEquals(10, stateThresholdList.get(2).thresholds[0].fromHour);
        assertEquals(0, stateThresholdList.get(2).thresholds[0].fromMinute);
        assertEquals(20, stateThresholdList.get(2).thresholds[0].toHour);
        assertEquals(0, stateThresholdList.get(2).thresholds[0].toMinute);
        long sundayMay1st6oclock = 1462082400000L;
        long sundayMay1st14oclock = 1462111200000L;
        long mondayMay2nd6oclock = 1462168800000L;
        long mondayMay2nd14oclock = 1462197600000L;
        assertFalse(stateThresholdList.get(2).thresholds[0].isInEffectAt(sundayMay1st6oclock));
        assertTrue(stateThresholdList.get(2).thresholds[0].isInEffectAt(sundayMay1st14oclock));
        assertFalse(stateThresholdList.get(2).thresholds[0].isInEffectAt(mondayMay2nd6oclock));
        assertFalse(stateThresholdList.get(2).thresholds[0].isInEffectAt(mondayMay2nd14oclock));
    }
}
