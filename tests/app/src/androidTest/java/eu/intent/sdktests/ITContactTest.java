package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITContact;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITContactTest {
    @Test
    public void jsonToContactList() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.contacts);
        Type listType = new TypeToken<List<ITContact>>() {
        }.getType();
        List<ITContact> contacts = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(2, contacts.size());
        assertEquals("Gardien", contacts.get(0).name);
        assertEquals("Michel", contacts.get(1).name);
        assertEquals("06.12.34.56.78", contacts.get(0).number);
        assertEquals("06.06.06.06.06", contacts.get(1).number);
        assertEquals(ITContact.Visibility.PUBLIC, contacts.get(0).visibility);
        assertEquals(ITContact.Visibility.PRIVATE, contacts.get(1).visibility);
    }
}
