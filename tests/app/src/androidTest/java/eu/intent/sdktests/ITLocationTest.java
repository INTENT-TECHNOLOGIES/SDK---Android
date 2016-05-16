package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITLocation;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ITLocationTest {
    @Test
    public void jsonToLocation() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.location_latlng);
        ITLocation location = ITRetrofitUtils.getGson().fromJson(json, ITLocation.class);
        assertEquals(49.5809623, location.lat);
        assertEquals(0.8337479, location.lng);
        assertTrue(location.isValid());
    }

    @Test
    public void jsonToLocationInvalid() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.location_latlng_invalid);
        ITLocation location = ITRetrofitUtils.getGson().fromJson(json, ITLocation.class);
        assertEquals(0.0, location.lat);
        assertEquals(0.0, location.lng);
        assertFalse(location.isValid());
    }

    @Test
    public void geoJsonToLocation() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.location_geojson);
        ITLocation location = ITRetrofitUtils.getGson().fromJson(json, ITLocation.class);
        assertEquals(49.5809623, location.lat);
        assertEquals(0.8337479, location.lng);
        assertTrue(location.isValid());
    }

    @Test
    public void geoJsonToLocationInvalid() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.location_geojson_invalid);
        ITLocation location = ITRetrofitUtils.getGson().fromJson(json, ITLocation.class);
        assertEquals(0.0, location.lat);
        assertEquals(0.0, location.lng);
        assertFalse(location.isValid());
    }
}
