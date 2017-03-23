package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITPart;
import eu.intent.sdk.model.ITPartList;

import static junit.framework.Assert.assertEquals;

public class ITPartTest {
    @Test
    public void jsonToOperationList() {
        String json = TestUtils.INSTANCE.getResourceAsString("parts.json");
        ITPartList partList = TestUtils.INSTANCE.getGson().fromJson(json, ITPartList.class);
        assertEquals(10, partList.totalCount);
        assertEquals(2, partList.parts.size());
        assertEquals("21", partList.parts.get(0).door);
        assertEquals("1", partList.parts.get(1).door);
        assertEquals("DEMO1-SITE-DEMO-TSH-28 21", partList.parts.get(0).externalRef);
        assertEquals("DEMO1-SITE-DEMO-TSH-28 1", partList.parts.get(1).externalRef);
        assertEquals("r+2", partList.parts.get(0).floor);
        assertEquals("rdc", partList.parts.get(1).floor);
        assertEquals("05eaf9cf-5ff6-4a23-b607-02a355d2ea9e", partList.parts.get(0).id);
        assertEquals("0a53a368-3d94-4dea-ac61-855b3e5fef0d", partList.parts.get(1).id);
        assertEquals("test", partList.parts.get(0).label);
        assertEquals(null, partList.parts.get(1).label);
        assertEquals("demo1", partList.parts.get(0).owner);
        assertEquals("demo1", partList.parts.get(1).owner);
        assertEquals("03f8e099-10d5-4521-93ef-c76325641216", partList.parts.get(0).siteId);
        assertEquals("03f8e099-10d5-4521-93ef-c76325641216", partList.parts.get(1).siteId);
        assertEquals(ITPart.ITPortion.PRIVATE, partList.parts.get(0).portion);
        assertEquals(ITPart.ITPortion.COMMON, partList.parts.get(1).portion);
        assertEquals(1, partList.parts.get(0).users.size());
        assertEquals(0, partList.parts.get(1).users.size());
        assertEquals("email@test.com", partList.parts.get(0).users.get(0).email);
        assertEquals("First", partList.parts.get(0).users.get(0).firstName);
        assertEquals("Last", partList.parts.get(0).users.get(0).lastName);
        assertEquals("05 05 05", partList.parts.get(0).users.get(0).phone);
        assertEquals("06 06 06", partList.parts.get(0).users.get(0).mobile);
    }
}
