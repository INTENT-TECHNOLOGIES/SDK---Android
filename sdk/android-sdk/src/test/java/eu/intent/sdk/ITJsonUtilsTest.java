package eu.intent.sdk;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import eu.intent.sdk.util.ITJsonUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ITJsonUtilsTest {
    private static final String KEY = "KEY";
    private static final String DEFAULT_VALUE = "DEFAULT_VALUE";

    @Test
    public void missingKeyToDate() {
        JsonObject jsonObject = new JsonObject();
        long timestamp = ITJsonUtils.parseJsonIsoDate(jsonObject, KEY);
        assertEquals("Missing key should result in a 0 timestamp", 0, timestamp);
    }

    @Test
    public void nullToDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(KEY, null);
        long timestamp = ITJsonUtils.parseJsonIsoDate(jsonObject, KEY);
        assertEquals("Null JSON value should result in a 0 timestamp", 0, timestamp);
    }

    @Test
    public void jsonNullToDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(KEY, JsonNull.INSTANCE);
        long timestamp = ITJsonUtils.parseJsonIsoDate(jsonObject, KEY);
        assertEquals("JsonNull should result in a 0 timestamp", 0, timestamp);
    }

    @Test
    public void jsonObjectToDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(KEY, new JsonObject());
        long timestamp = ITJsonUtils.parseJsonIsoDate(jsonObject, KEY);
        assertEquals("JsonObject should result in a 0 timestamp", 0, timestamp);
    }

    @Test
    public void jsonArrayToDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(KEY, new JsonArray());
        long timestamp = ITJsonUtils.parseJsonIsoDate(jsonObject, KEY);
        assertEquals("JsonArray should result in a 0 timestamp", 0, timestamp);
    }

    @Test
    public void emptyStringToDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KEY, "");
        long timestamp = ITJsonUtils.parseJsonIsoDate(jsonObject, KEY);
        assertEquals("Empty string should result in a 0 timestamp", 0, timestamp);
    }

    @Test
    public void numberToDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KEY, 123);
        long timestamp = ITJsonUtils.parseJsonIsoDate(jsonObject, KEY);
        assertEquals("Number should result in a 0 timestamp", 0, timestamp);
    }

    @Test
    public void randomStringToDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KEY, "abc");
        long timestamp = ITJsonUtils.parseJsonIsoDate(jsonObject, KEY);
        assertEquals("Random string should result in a 0 timestamp", 0, timestamp);
    }

    @Test
    public void correctStringToDate() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KEY, "2017-04-05T11:40:30.123Z");
        long timestamp = ITJsonUtils.parseJsonIsoDate(jsonObject, KEY);
        assertTrue("Correct string should result in a relevant timestamp", timestamp > 0);
    }

    @Test
    public void missingKeyToString() {
        JsonObject jsonObject = new JsonObject();
        String string = ITJsonUtils.getJsonAsString(jsonObject, KEY, DEFAULT_VALUE);
        assertEquals("Missing key should result in default value", DEFAULT_VALUE, string);
    }

    @Test
    public void nullToString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(KEY, null);
        String string = ITJsonUtils.getJsonAsString(jsonObject, KEY, DEFAULT_VALUE);
        assertEquals("Null value should result in default value", DEFAULT_VALUE, string);
    }

    @Test
    public void jsonNullToString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(KEY, JsonNull.INSTANCE);
        String string = ITJsonUtils.getJsonAsString(jsonObject, KEY, DEFAULT_VALUE);
        assertEquals("JsonNull should result in default value", DEFAULT_VALUE, string);
    }

    @Test
    public void jsonObjectToString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(KEY, new JsonObject());
        String string = ITJsonUtils.getJsonAsString(jsonObject, KEY, DEFAULT_VALUE);
        assertEquals("JsonObject should result in default value", DEFAULT_VALUE, string);
    }

    @Test
    public void jsonArrayToString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(KEY, new JsonArray());
        String string = ITJsonUtils.getJsonAsString(jsonObject, KEY, DEFAULT_VALUE);
        assertEquals("JsonArray should result in default value", DEFAULT_VALUE, string);
    }

    @Test
    public void numberToString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KEY, 123);
        String string = ITJsonUtils.getJsonAsString(jsonObject, KEY, DEFAULT_VALUE);
        assertEquals("Number should result in a parsed value", "123", string);
    }

    @Test
    public void booleanToString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KEY, false);
        String string = ITJsonUtils.getJsonAsString(jsonObject, KEY, DEFAULT_VALUE);
        assertEquals("Boolean should result in a parsed value", "false", string);
    }

    @Test
    public void stringToString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(KEY, "123");
        String string = ITJsonUtils.getJsonAsString(jsonObject, KEY, DEFAULT_VALUE);
        assertEquals("Number should result in default value", "123", string);
    }
}
