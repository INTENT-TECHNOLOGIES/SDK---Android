package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITActivity;
import eu.intent.sdk.model.ITActivityCategory;
import eu.intent.sdk.model.ITActivityKeys;
import eu.intent.sdk.model.ITActivityList;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITActivityTest {
    @Test
    public void jsonToActivityList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.activities);
        ITActivityList activityList = ITRetrofitUtils.getGson().fromJson(json, ITActivityList.class);
        assertEquals(2, activityList.totalCount);
        assertEquals(2, activityList.activities.size());
        assertEquals("HeatingElecCons", activityList.activities.get(0).id);
        assertEquals("VmcPressure", activityList.activities.get(1).id);
        assertEquals("Consommation électrique du chauffage en kilowatt-heure", activityList.activities.get(0).description);
        assertEquals("Pression moyenne VMC", activityList.activities.get(1).description);
        assertEquals("Conso élec chauffage", activityList.activities.get(0).label);
        assertEquals("Pression moyenne VMC", activityList.activities.get(1).label);
        assertEquals(4, activityList.activities.get(0).tags.size());
        assertEquals(4, activityList.activities.get(1).tags.size());
        assertEquals("elecConso", activityList.activities.get(0).tags.get("measureType"));
        assertEquals("airPressure", activityList.activities.get(1).tags.get("measureType"));
        assertEquals("kwh", activityList.activities.get(0).tags.get("unit"));
        assertEquals("Pa", activityList.activities.get(1).tags.get("unit"));
        assertEquals("heat", activityList.activities.get(0).tags.get("function"));
        assertEquals("VMC", activityList.activities.get(1).tags.get("function"));
        assertEquals("elec", activityList.activities.get(0).tags.get("icon"));
        assertEquals("air", activityList.activities.get(1).tags.get("icon"));
    }

    @Test
    public void jsonToActivityCategories() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.activities_by_theme);
        Type listType = new TypeToken<List<ITActivityCategory>>() {
        }.getType();
        List<ITActivityCategory> activityCategories = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(2, activityCategories.size());
        assertEquals("HEATING", activityCategories.get(0).id);
        assertEquals("LIFT", activityCategories.get(1).id);
        assertEquals("Chauffage", activityCategories.get(0).label);
        assertEquals("Ascenseur", activityCategories.get(1).label);
        assertEquals("heat", activityCategories.get(0).icon);
        assertEquals("lift", activityCategories.get(1).icon);
        assertEquals(11, activityCategories.get(0).activities.length);
        assertEquals(3, activityCategories.get(1).activities.length);
    }

    @Test
    public void jsonToActivity() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.activity);
        ITActivity activity = ITRetrofitUtils.getGson().fromJson(json, ITActivity.class);
        assertEquals("HeatingElecCons", activity.id);
        assertEquals("Conso élec chauffage", activity.label);
        assertEquals("Consommation électrique du chauffage en kilowatt-heure", activity.description);
        assertEquals(4, activity.tags.size());
        assertEquals("elecConso", activity.tags.get("measureType"));
        assertEquals("kwh", activity.tags.get("unit"));
        assertEquals("heat", activity.tags.get("function"));
        assertEquals("elec", activity.tags.get("icon"));
    }

    @Test
    public void jsonToActivityKeys() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.activity_keys);
        ITActivityKeys activityKeys = ITRetrofitUtils.getGson().fromJson(json, ITActivityKeys.class);
        assertEquals(13, activityKeys.activities.size());
    }
}
