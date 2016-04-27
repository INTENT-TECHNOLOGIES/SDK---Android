package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITStateTemplate;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITStateTemplateTest {
    @Test
    public void jsonToStateTemplates() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.state_templates);
        Type listType = new TypeToken<List<ITStateTemplate>>() {
        }.getType();
        List<ITStateTemplate> stateTemplateList = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(3, stateTemplateList.size());
        assertEquals("thresholdCrossing", stateTemplateList.get(0).function);
        assertEquals("visit-counter", stateTemplateList.get(1).function);
        assertEquals("ve-done", stateTemplateList.get(2).function);
        assertEquals("ThresholdCrossing-AirTemp-Max", stateTemplateList.get(0).id);
        assertEquals("Visit-Counter-Intervention", stateTemplateList.get(1).id);
        assertEquals("VE-Done-Intervention", stateTemplateList.get(2).id);
        assertEquals("Inconfort : trop chaud", stateTemplateList.get(0).label);
        assertEquals("Nombre de visites effectuées", stateTemplateList.get(1).label);
        assertEquals("Visite d'entretien effectuée", stateTemplateList.get(2).label);
        assertEquals("NORMAL", stateTemplateList.get(0).statusDefault);
        assertEquals("0", stateTemplateList.get(1).statusDefault);
        assertEquals("NO", stateTemplateList.get(2).statusDefault);
        assertEquals(3, stateTemplateList.get(0).statusEnum.length);
        assertEquals(7, stateTemplateList.get(1).statusEnum.length);
        assertEquals(2, stateTemplateList.get(2).statusEnum.length);
        assertEquals(1, stateTemplateList.get(0).activities.length);
        assertEquals(1, stateTemplateList.get(1).activities.length);
        assertEquals(1, stateTemplateList.get(2).activities.length);
        assertEquals("AirTemp", stateTemplateList.get(0).activities[0]);
        assertEquals("Intervention", stateTemplateList.get(1).activities[0]);
        assertEquals("Intervention", stateTemplateList.get(2).activities[0]);
    }
}
