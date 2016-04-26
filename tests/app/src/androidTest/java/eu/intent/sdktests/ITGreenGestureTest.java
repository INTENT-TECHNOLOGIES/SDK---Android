package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITEnergy;
import eu.intent.sdk.model.ITGreenGestureList;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITGreenGestureTest {
    @Test
    public void jsonToGreenGestureList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.green_gestures);
        ITGreenGestureList greenGestureList = ITRetrofitUtils.getGson().fromJson(json, ITGreenGestureList.class);
        assertEquals(1, greenGestureList.totalCount);
        assertEquals(1, greenGestureList.gestures.size());
        assertEquals(1, greenGestureList.gestures.get(0).duration);
        assertEquals(false, greenGestureList.gestures.get(0).enabled);
        assertEquals(ITEnergy.ELEC, greenGestureList.gestures.get(0).energy);
        assertEquals("a27f8370-6029-4514-9488-746925c08f8d", greenGestureList.gestures.get(0).id);
        assertEquals(5, greenGestureList.gestures.get(0).savings);
        assertEquals("desc", greenGestureList.gestures.get(0).text);
        assertEquals("title", greenGestureList.gestures.get(0).title);
    }
}
