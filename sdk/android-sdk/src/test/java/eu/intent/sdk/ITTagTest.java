package eu.intent.sdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import eu.intent.sdk.model.ITTagList;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ITTagTest {
    @Test
    public void jsonToTagList() {
        String json = TestUtils.INSTANCE.getResourceAsString("tags.json");
        ITTagList tagList = TestUtils.INSTANCE.getGson().fromJson(json, ITTagList.class);
        assertEquals(126, tagList.tags.size());
        assertEquals("measureType", tagList.get("waterConso").category);
        assertEquals("waterConso", tagList.get("waterConso").key);
        assertEquals("system", tagList.get("waterConso").owner);
        assertEquals("Conso eau", tagList.get("waterConso").getShortLabel("fr"));
        assertEquals("Consommation d'eau", tagList.get("waterConso").getLabel("fr"));
    }
}
