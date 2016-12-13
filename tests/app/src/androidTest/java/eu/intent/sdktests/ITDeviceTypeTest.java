package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITDeviceType;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ITDeviceTypeTest {
    @Test
    public void jsonToDeviceTypeList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.device_types);
        Type listType = new TypeToken<List<ITDeviceType>>() {
        }.getType();
        List<ITDeviceType> deviceTypes = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(6, deviceTypes.size());
        assertEquals("concentrator", deviceTypes.get(0).name);
        assertEquals("watersystemsensor", deviceTypes.get(1).name);
        assertEquals("elecsensor", deviceTypes.get(2).name);
        assertEquals("airflowsensor", deviceTypes.get(3).name);
        assertEquals("repetitor", deviceTypes.get(4).name);
        assertEquals("airflowsensor", deviceTypes.get(5).name);
        assertEquals("nke", deviceTypes.get(0).vendor);
        assertEquals("nke", deviceTypes.get(1).vendor);
        assertEquals("nke", deviceTypes.get(2).vendor);
        assertEquals("nke", deviceTypes.get(3).vendor);
        assertEquals("nke", deviceTypes.get(4).vendor);
        assertEquals("nkesigfox", deviceTypes.get(5).vendor);
        assertEquals("nke_concentrator", deviceTypes.get(0).fullName);
        assertEquals("nke_watersystemsensor", deviceTypes.get(1).fullName);
        assertEquals("nke_elecsensor", deviceTypes.get(2).fullName);
        assertEquals("nke_airflowsensor", deviceTypes.get(3).fullName);
        assertEquals("nke_repetitor", deviceTypes.get(4).fullName);
        assertEquals("nkesigfox_airflowsensor", deviceTypes.get(5).fullName);
        assertEquals("50-09-001", deviceTypes.get(0).number);
        assertEquals("", deviceTypes.get(1).number);
        assertEquals("50-09-009 ou 50-09-019", deviceTypes.get(2).number);
        assertEquals("50-09-030", deviceTypes.get(3).number);
        assertEquals("", deviceTypes.get(4).number);
        assertEquals("", deviceTypes.get(5).number);
        assertEquals(true, deviceTypes.get(0).wired);
        assertEquals(false, deviceTypes.get(1).wired);
        assertEquals(true, deviceTypes.get(2).wired);
        assertEquals(false, deviceTypes.get(3).wired);
        assertEquals(false, deviceTypes.get(4).wired);
        assertEquals(false, deviceTypes.get(5).wired);
        assertEquals(1, deviceTypes.get(0).functions.size());
        assertEquals(1, deviceTypes.get(1).functions.size());
        assertEquals(1, deviceTypes.get(2).functions.size());
        assertEquals(1, deviceTypes.get(3).functions.size());
        assertEquals(1, deviceTypes.get(4).functions.size());
        assertEquals(2, deviceTypes.get(5).functions.size());
        assertEquals("communication", deviceTypes.get(0).functions.get(0));
        assertEquals("measure", deviceTypes.get(1).functions.get(0));
        assertEquals("measure", deviceTypes.get(2).functions.get(0));
        assertEquals("measure", deviceTypes.get(3).functions.get(0));
        assertEquals("repeat", deviceTypes.get(4).functions.get(0));
        assertEquals("measure", deviceTypes.get(5).functions.get(0));
        assertEquals("communication", deviceTypes.get(5).functions.get(1));
        assertEquals("^urn:intent:010_\\d{6}$", deviceTypes.get(0).idPattern.regex);
        assertEquals("^urn:intent:093_\\d{6}$", deviceTypes.get(1).idPattern.regex);
        assertEquals("^urn:intent:084_\\d{6}$", deviceTypes.get(2).idPattern.regex);
        assertEquals("^urn:intent:092_\\d{6}$", deviceTypes.get(3).idPattern.regex);
        assertEquals("^urn:intent:150_\\d{6}$", deviceTypes.get(4).idPattern.regex);
        assertEquals("^urn:intent:200_[0-9A-Fa-f]{8}$", deviceTypes.get(5).idPattern.regex);
        assertEquals("urn:intent:010_", deviceTypes.get(0).idPattern.prefix);
        assertEquals("urn:intent:093_", deviceTypes.get(1).idPattern.prefix);
        assertEquals("urn:intent:084_", deviceTypes.get(2).idPattern.prefix);
        assertEquals("urn:intent:092_", deviceTypes.get(3).idPattern.prefix);
        assertEquals("urn:intent:150_", deviceTypes.get(4).idPattern.prefix);
        assertEquals("urn:intent:200_", deviceTypes.get(5).idPattern.prefix);
        assertEquals("0", deviceTypes.get(0).idPattern.placeholderChar);
        assertEquals("0", deviceTypes.get(1).idPattern.placeholderChar);
        assertEquals("0", deviceTypes.get(2).idPattern.placeholderChar);
        assertEquals("0", deviceTypes.get(3).idPattern.placeholderChar);
        assertEquals("0", deviceTypes.get(4).idPattern.placeholderChar);
        assertEquals("0", deviceTypes.get(5).idPattern.placeholderChar);
        assertEquals(6, deviceTypes.get(0).idPattern.variableSize);
        assertEquals(6, deviceTypes.get(1).idPattern.variableSize);
        assertEquals(6, deviceTypes.get(2).idPattern.variableSize);
        assertEquals(6, deviceTypes.get(3).idPattern.variableSize);
        assertEquals(6, deviceTypes.get(4).idPattern.variableSize);
        assertEquals(8, deviceTypes.get(5).idPattern.variableSize);
        assertEquals("urn:intent:", deviceTypes.get(0).idPattern.getBrandPrefix());
        assertEquals("urn:intent:", deviceTypes.get(1).idPattern.getBrandPrefix());
        assertEquals("urn:intent:", deviceTypes.get(2).idPattern.getBrandPrefix());
        assertEquals("urn:intent:", deviceTypes.get(3).idPattern.getBrandPrefix());
        assertEquals("urn:intent:", deviceTypes.get(4).idPattern.getBrandPrefix());
        assertEquals("urn:intent:", deviceTypes.get(5).idPattern.getBrandPrefix());
        assertEquals("010_", deviceTypes.get(0).idPattern.getShortPrefix());
        assertEquals("093_", deviceTypes.get(1).idPattern.getShortPrefix());
        assertEquals("084_", deviceTypes.get(2).idPattern.getShortPrefix());
        assertEquals("092_", deviceTypes.get(3).idPattern.getShortPrefix());
        assertEquals("150_", deviceTypes.get(4).idPattern.getShortPrefix());
        assertEquals("200_", deviceTypes.get(5).idPattern.getShortPrefix());
        assertEquals(0, deviceTypes.get(0).outputs.size());
        assertEquals(2, deviceTypes.get(1).outputs.size());
        assertEquals(6, deviceTypes.get(2).outputs.size());
        assertEquals(3, deviceTypes.get(3).outputs.size());
        assertEquals(0, deviceTypes.get(4).outputs.size());
        assertEquals(1, deviceTypes.get(5).outputs.size());
        assertEquals("PULSE", deviceTypes.get(1).outputs.get(0).key);
        assertEquals("TELEINFO", deviceTypes.get(2).outputs.get(0).key);
        assertEquals("HYGRO", deviceTypes.get(3).outputs.get(0).key);
        assertEquals("PRESS", deviceTypes.get(5).outputs.get(0).key);
        assertEquals(false, deviceTypes.get(1).outputs.get(0).required);
        assertEquals(false, deviceTypes.get(2).outputs.get(0).required);
        assertEquals(true, deviceTypes.get(3).outputs.get(0).required);
        assertEquals(true, deviceTypes.get(5).outputs.get(0).required);
        assertEquals(4, deviceTypes.get(1).outputs.get(0).activities.size());
        assertEquals(12, deviceTypes.get(2).outputs.get(0).activities.size());
        assertEquals(2, deviceTypes.get(3).outputs.get(0).activities.size());
        assertEquals(1, deviceTypes.get(5).outputs.get(0).activities.size());
        assertEquals(2, deviceTypes.get(1).outputs.get(0).params.size());
        assertEquals(2, deviceTypes.get(2).outputs.get(0).params.size());
        assertEquals(0, deviceTypes.get(3).outputs.get(0).params.size());
        assertEquals(0, deviceTypes.get(5).outputs.get(0).params.size());
        assertEquals("k", deviceTypes.get(1).outputs.get(0).params.get(0).key);
        assertEquals("beginIndex", deviceTypes.get(2).outputs.get(0).params.get(1).key);
        assertEquals("^[-+]?[0-9]*\\.?[0-9]+$", deviceTypes.get(1).outputs.get(0).params.get(0).pattern);
        assertEquals("^[-+]?[0-9]*\\.?[0-9]+$", deviceTypes.get(2).outputs.get(0).params.get(1).pattern);
        assertNotNull(deviceTypes.get(0).scanTags);
        assertTrue(deviceTypes.get(0).scanTags.isEmpty());
        assertEquals(3, deviceTypes.get(5).scanTags.size());
        assertEquals(ITDeviceType.ScanTag.TYPE_NFC, deviceTypes.get(5).scanTags.get(0).type);
        assertEquals(ITDeviceType.ScanTag.TYPE_QRCODE, deviceTypes.get(5).scanTags.get(1).type);
        assertEquals("unknown", deviceTypes.get(5).scanTags.get(2).type);
        assertNull(deviceTypes.get(5).scanTags.get(0).decoder);
        assertNotNull(deviceTypes.get(5).scanTags.get(1).decoder);
        assertNotNull(deviceTypes.get(5).scanTags.get(2).decoder);
        assertEquals(ITDeviceType.ScanTag.Decoder.TYPE_URL, deviceTypes.get(5).scanTags.get(1).decoder.type);
        assertEquals("unknown", deviceTypes.get(5).scanTags.get(2).decoder.type);
        assertEquals("http://fakeapi.org/qrcode/{content}", deviceTypes.get(5).scanTags.get(1).decoder.data);
        assertNull(deviceTypes.get(5).scanTags.get(2).decoder.data);
    }

    @Test
    public void testGetScanTagWithNullList() {
        ITDeviceType deviceType = new ITDeviceType();
        deviceType.scanTags = null;
        assertNull(deviceType.getScanTag("whatever"));
    }

    @Test
    public void testGetScanTagWithEmptyList() {
        ITDeviceType deviceType = new ITDeviceType();
        deviceType.scanTags = new ArrayList<>();
        assertNull(deviceType.getScanTag("whatever"));
    }

    @Test
    public void testGetScanTagWithNoMatchingType() {
        ITDeviceType.ScanTag tag1 = new ITDeviceType.ScanTag();
        ITDeviceType.ScanTag tag2 = new ITDeviceType.ScanTag();
        tag1.type = "type1";
        tag2.type = "type2";
        ITDeviceType deviceType = new ITDeviceType();
        deviceType.scanTags = new ArrayList<>();
        deviceType.scanTags.add(tag1);
        deviceType.scanTags.add(tag2);
        assertNull(deviceType.getScanTag("type3"));
    }

    @Test
    public void testGetScanTagWithMatchingType() {
        ITDeviceType.ScanTag tag1 = new ITDeviceType.ScanTag();
        ITDeviceType.ScanTag tag2 = new ITDeviceType.ScanTag();
        tag1.type = "type1";
        tag2.type = "type2";
        ITDeviceType deviceType = new ITDeviceType();
        deviceType.scanTags = new ArrayList<>();
        deviceType.scanTags.add(tag1);
        deviceType.scanTags.add(tag2);
        assertEquals(tag2, deviceType.getScanTag("type2"));
    }

    @Test
    public void testGetScanTagWithSeveralMatchingType() {
        ITDeviceType.ScanTag tag1 = new ITDeviceType.ScanTag();
        ITDeviceType.ScanTag tag2 = new ITDeviceType.ScanTag();
        tag1.type = "type1";
        tag2.type = "type1";
        ITDeviceType deviceType = new ITDeviceType();
        deviceType.scanTags = new ArrayList<>();
        deviceType.scanTags.add(tag1);
        deviceType.scanTags.add(tag2);
        ITDeviceType.ScanTag result = deviceType.getScanTag("type1");
        assertNotNull(result);
        assertTrue(result.equals(tag1) || result.equals(tag2));
    }

    @Test
    public void testScanTagGetDecodeUrlWithNullTagType() {
        ITDeviceType.ScanTag.Decoder tagDecoder = new ITDeviceType.ScanTag.Decoder();
        tagDecoder.type = null;
        tagDecoder.data = "http://fakedecoder.org/{content}";
        String decodeUrl = tagDecoder.getDecodeUrl("123");
        assertEquals("", decodeUrl);
    }

    @Test
    public void testScanTagGetDecodeUrlWithWrongTagType() {
        ITDeviceType.ScanTag.Decoder tagDecoder = new ITDeviceType.ScanTag.Decoder();
        tagDecoder.type = "wrong_type";
        tagDecoder.data = "http://fakedecoder.org/{content}";
        String decodeUrl = tagDecoder.getDecodeUrl("123");
        assertEquals("", decodeUrl);
    }

    @Test
    public void testScanTagGetDecodeUrlWithNullUrl() {
        ITDeviceType.ScanTag.Decoder tagDecoder = new ITDeviceType.ScanTag.Decoder();
        tagDecoder.type = ITDeviceType.ScanTag.Decoder.TYPE_URL;
        tagDecoder.data = null;
        String decodeUrl = tagDecoder.getDecodeUrl("123");
        assertEquals("", decodeUrl);
    }

    @Test
    public void testScanTagGetDecodeUrlWithEmptyUrl() {
        ITDeviceType.ScanTag.Decoder tagDecoder = new ITDeviceType.ScanTag.Decoder();
        tagDecoder.type = ITDeviceType.ScanTag.Decoder.TYPE_URL;
        tagDecoder.data = "";
        String decodeUrl = tagDecoder.getDecodeUrl("123");
        assertEquals("", decodeUrl);
    }

    @Test
    public void testScanTagGetDecodeUrlWithoutPlaceholder() {
        ITDeviceType.ScanTag.Decoder tagDecoder = new ITDeviceType.ScanTag.Decoder();
        tagDecoder.type = ITDeviceType.ScanTag.Decoder.TYPE_URL;
        tagDecoder.data = "http://fakedecoder.org/{wrong_placeholder}";
        String decodeUrl = tagDecoder.getDecodeUrl("123");
        assertEquals("http://fakedecoder.org/{wrong_placeholder}", decodeUrl);
    }

    @Test
    public void testScanTagGetDecodeUrlWithNullContent() {
        ITDeviceType.ScanTag.Decoder tagDecoder = new ITDeviceType.ScanTag.Decoder();
        tagDecoder.type = ITDeviceType.ScanTag.Decoder.TYPE_URL;
        tagDecoder.data = "http://fakedecoder.org/{content}";
        String decodeUrl = tagDecoder.getDecodeUrl(null);
        assertEquals("http://fakedecoder.org/{content}", decodeUrl);
    }

    @Test
    public void testScanTagGetDecodeUrlWithCorrectUrlAndContent() {
        ITDeviceType.ScanTag.Decoder tagDecoder = new ITDeviceType.ScanTag.Decoder();
        tagDecoder.type = ITDeviceType.ScanTag.Decoder.TYPE_URL;
        tagDecoder.data = "http://fakedecoder.org/{content}";
        String decodeUrl = tagDecoder.getDecodeUrl("123");
        assertEquals("http://fakedecoder.org/123", decodeUrl);
    }
}
