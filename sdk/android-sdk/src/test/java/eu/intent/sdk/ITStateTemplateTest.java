package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITStateTemplate;

import static junit.framework.Assert.assertEquals;

public class ITStateTemplateTest {
    @Test
    public void jsonToStateTemplates() {
        String json = TestUtils.INSTANCE.getResourceAsString("state_templates.json");
        ITStateTemplate[] stateTemplateList = TestUtils.INSTANCE.getGson().fromJson(json, ITStateTemplate[].class);
        assertEquals(3, stateTemplateList.length);
        assertEquals("thresholdCrossing", stateTemplateList[0].function);
        assertEquals("visit-counter", stateTemplateList[1].function);
        assertEquals("ve-done", stateTemplateList[2].function);
        assertEquals("ThresholdCrossing-AirTemp-Max", stateTemplateList[0].id);
        assertEquals("Visit-Counter-Intervention", stateTemplateList[1].id);
        assertEquals("VE-Done-Intervention", stateTemplateList[2].id);
        assertEquals("Inconfort : trop chaud", stateTemplateList[0].label);
        assertEquals("Nombre de visites effectuées", stateTemplateList[1].label);
        assertEquals("Visite d'entretien effectuée", stateTemplateList[2].label);
        assertEquals("NORMAL", stateTemplateList[0].statusDefault);
        assertEquals("0", stateTemplateList[1].statusDefault);
        assertEquals("NO", stateTemplateList[2].statusDefault);
        assertEquals(3, stateTemplateList[0].statusEnum.length);
        assertEquals(7, stateTemplateList[1].statusEnum.length);
        assertEquals(2, stateTemplateList[2].statusEnum.length);
        assertEquals(1, stateTemplateList[0].activities.length);
        assertEquals(1, stateTemplateList[1].activities.length);
        assertEquals(1, stateTemplateList[2].activities.length);
        assertEquals("AirTemp", stateTemplateList[0].activities[0]);
        assertEquals("Intervention", stateTemplateList[1].activities[0]);
        assertEquals("Intervention", stateTemplateList[2].activities[0]);
    }
}
