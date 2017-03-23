package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITSiteList;

import static junit.framework.Assert.assertEquals;

public class ITSiteTest {
    @Test
    public void jsonToSiteList() {
        String json = TestUtils.INSTANCE.getResourceAsString("sites.json");
        ITSiteList siteList = TestUtils.INSTANCE.getGson().fromJson(json, ITSiteList.class);
        assertEquals(30, siteList.totalCount);
        assertEquals(2, siteList.sites.size());
        assertEquals("DEMO1-SITE-DEMO-TSH-28", siteList.sites.get(0).externalRef);
        assertEquals("DEMO1-SITE-DEMO-TSH-04", siteList.sites.get(1).externalRef);
        assertEquals("03f8e099-10d5-4521-93ef-c76325641216", siteList.sites.get(0).id);
        assertEquals("05b7bfee-7272-4b9a-a76e-3b3fb53b80c8", siteList.sites.get(1).id);
        assertEquals(null, siteList.sites.get(0).label);
        assertEquals("test", siteList.sites.get(1).label);
        assertEquals("demo1", siteList.sites.get(0).owner);
        assertEquals("demo1", siteList.sites.get(1).owner);
    }
}
