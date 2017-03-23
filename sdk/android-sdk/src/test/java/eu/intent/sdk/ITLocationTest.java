package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITLocation;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ITLocationTest {
    @Test
    public void jsonToLocation() {
        String json = TestUtils.INSTANCE.getResourceAsString("location_latlng.json");
        ITLocation location = TestUtils.INSTANCE.getGson().fromJson(json, ITLocation.class);
        assertEquals(49.5809623, location.lat);
        assertEquals(0.8337479, location.lng);
        assertTrue(location.isValid());
    }

    @Test
    public void jsonToLocationInvalid() {
        String json = TestUtils.INSTANCE.getResourceAsString("location_latlng_invalid.json");
        ITLocation location = TestUtils.INSTANCE.getGson().fromJson(json, ITLocation.class);
        assertEquals(0.0, location.lat);
        assertEquals(0.0, location.lng);
        assertFalse(location.isValid());
    }

    @Test
    public void geoJsonToLocation() {
        String json = TestUtils.INSTANCE.getResourceAsString("location_geojson.json");
        ITLocation location = TestUtils.INSTANCE.getGson().fromJson(json, ITLocation.class);
        assertEquals(49.5809623, location.lat);
        assertEquals(0.8337479, location.lng);
        assertTrue(location.isValid());
    }

    @Test
    public void geoJsonToLocationInvalid() {
        String json = TestUtils.INSTANCE.getResourceAsString("location_geojson_invalid.json");
        ITLocation location = TestUtils.INSTANCE.getGson().fromJson(json, ITLocation.class);
        assertEquals(0.0, location.lat);
        assertEquals(0.0, location.lng);
        assertFalse(location.isValid());
    }
}
