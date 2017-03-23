package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITAddress;

import static junit.framework.Assert.assertEquals;

public class ITAddressTest {
    @Test
    public void jsonToAddress() {
        String json = TestUtils.INSTANCE.getResourceAsString("address.json");
        ITAddress address = TestUtils.INSTANCE.getGson().fromJson(json, ITAddress.class);
        assertEquals("Fréville", address.city);
        assertEquals("Le Clos du mont", address.complement);
        assertEquals("FR", address.country);
        assertEquals("450", address.num);
        assertEquals("Route de la Cousinerie", address.way);
        assertEquals("76190", address.zip);
    }
}
