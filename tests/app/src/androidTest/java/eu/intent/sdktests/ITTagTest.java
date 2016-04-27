package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITTagList;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITTagTest {
    @Test
    public void jsonToTagList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.tags);
        ITTagList tagList = ITRetrofitUtils.getGson().fromJson(json, ITTagList.class);
        assertEquals(126, tagList.tags.size());
        assertEquals("measureType", tagList.get("waterConso").category);
        assertEquals("waterConso", tagList.get("waterConso").key);
        assertEquals("system", tagList.get("waterConso").owner);
        assertEquals("Conso eau", tagList.get("waterConso").getShortLabel("fr"));
        assertEquals("Consommation d'eau", tagList.get("waterConso").getLabel("fr"));
    }
}
