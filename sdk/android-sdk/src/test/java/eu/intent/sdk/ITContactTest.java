package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITContact;

import static junit.framework.Assert.assertEquals;

public class ITContactTest {
    @Test
    public void jsonToContactList() {
        String json = TestUtils.INSTANCE.getResourceAsString("contacts.json");
        ITContact[] contacts = TestUtils.INSTANCE.getGson().fromJson(json, ITContact[].class);
        assertEquals(2, contacts.length);
        assertEquals("Gardien", contacts[0].name);
        assertEquals("Michel", contacts[1].name);
        assertEquals("06.12.34.56.78", contacts[0].number);
        assertEquals("06.06.06.06.06", contacts[1].number);
        assertEquals(ITContact.Visibility.PUBLIC, contacts[0].visibility);
        assertEquals(ITContact.Visibility.PRIVATE, contacts[1].visibility);
    }
}
