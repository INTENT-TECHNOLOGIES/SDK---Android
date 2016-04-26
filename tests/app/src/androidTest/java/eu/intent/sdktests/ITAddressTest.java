package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITAddress;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITAddressTest {
    @Test
    public void jsonToAddress() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.address);
        ITAddress address = ITRetrofitUtils.getGson().fromJson(json, ITAddress.class);
        assertEquals("Fréville", address.city);
        assertEquals("Le Clos du mont", address.complement);
        assertEquals("FR", address.country);
        assertEquals("450", address.num);
        assertEquals("Route de la Cousinerie", address.way);
        assertEquals("76190", address.zip);
    }
}
