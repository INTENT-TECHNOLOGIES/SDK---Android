package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITActivity;
import eu.intent.sdk.model.ITActivityCategory;
import eu.intent.sdk.model.ITActivityKeys;
import eu.intent.sdk.model.ITActivityList;

import static junit.framework.Assert.assertEquals;

public class ITActivityTest {
    @Test
    public void jsonToActivityList() {
        String json = TestUtils.INSTANCE.getResourceAsString("activities.json");
        ITActivityList activityList = TestUtils.INSTANCE.getGson().fromJson(json, ITActivityList.class);
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
        String json = TestUtils.INSTANCE.getResourceAsString("activities_by_theme.json");
        ITActivityCategory[] activityCategories = TestUtils.INSTANCE.getGson().fromJson(json, ITActivityCategory[].class);
        assertEquals(2, activityCategories.length);
        assertEquals("HEATING", activityCategories[0].id);
        assertEquals("LIFT", activityCategories[1].id);
        assertEquals("Chauffage", activityCategories[0].label);
        assertEquals("Ascenseur", activityCategories[1].label);
        assertEquals("heat", activityCategories[0].icon);
        assertEquals("lift", activityCategories[1].icon);
        assertEquals(11, activityCategories[0].activities.length);
        assertEquals(3, activityCategories[1].activities.length);
    }

    @Test
    public void jsonToActivity() {
        String json = TestUtils.INSTANCE.getResourceAsString("activity.json");
        ITActivity activity = TestUtils.INSTANCE.getGson().fromJson(json, ITActivity.class);
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
        String json = TestUtils.INSTANCE.getResourceAsString("activity_keys.json");
        ITActivityKeys activityKeys = TestUtils.INSTANCE.getGson().fromJson(json, ITActivityKeys.class);
        assertEquals(13, activityKeys.activities.size());
    }
}
