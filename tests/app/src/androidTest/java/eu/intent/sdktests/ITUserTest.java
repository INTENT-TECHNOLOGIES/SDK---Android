package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITUser;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITUserTest {
    @Test
    public void jsonToUser() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.user);
        ITUser user = ITRetrofitUtils.getGson().fromJson(json, ITUser.class);
        assertEquals("test@intent.com", user.email);
        assertEquals("0687737480", user.mobile);
        assertEquals("05 05 05", user.phone);
        assertEquals("Démo", user.lastName);
        assertEquals("Intent", user.firstName);
        assertEquals("demo1.intent", user.domain);
        assertEquals("09a4f835-85d7-4f82-9f09-0bb16b58c930", user.id);
        assertEquals("admin.demo1", user.username);
        assertEquals(1, user.entityRoles.size());
        assertEquals(1, user.userRoles.size());
        assertEquals("client", user.entityRoles.get(0));
        assertEquals("admin", user.userRoles.get(0));
    }

    @Test
    public void testGetFullName() {
        ITUser user = new ITUser();
        user.username = "t.gallinari";
        assertEquals("t.gallinari", user.getFullName());
        user.firstName = "Thomas";
        assertEquals("Thomas", user.getFullName());
        user.firstName = "";
        user.lastName = "Gallinari";
        assertEquals("Gallinari", user.getFullName());
        user.firstName = "Thomas";
        assertEquals("Thomas Gallinari", user.getFullName());
    }

    @Test
    public void testGetShortName() {
        ITUser user = new ITUser();
        user.username = "t.gallinari";
        assertEquals("t.gallinari", user.getShortName());
        user.firstName = "Thomas";
        assertEquals("Thomas", user.getShortName());
        user.firstName = "";
        user.lastName = "Gallinari";
        assertEquals("G.", user.getShortName());
        user.firstName = "Thomas";
        assertEquals("Thomas G.", user.getShortName());
    }

    @Test
    public void testGetOnePhone() {
        ITUser user = new ITUser();
        assertEquals(null, user.getOnePhone());
        user.mobile = "06 06 06";
        assertEquals("06 06 06", user.getOnePhone());
        user.mobile = "";
        user.phone = "07 07 07";
        assertEquals("07 07 07", user.getOnePhone());
        user.mobile = "06 06 06";
        assertEquals("06 06 06", user.getOnePhone());
    }

    @Test
    public void testIsHomeAccount() {
        ITUser user = new ITUser();
        assertEquals(false, user.isHomeAccount());
        user.domain = "test.intent";
        assertEquals(false, user.isHomeAccount());
        user.domain = "test.homes.intent";
        assertEquals(true, user.isHomeAccount());
    }

    @Test
    public void testGetParentDomain() {
        ITUser user = new ITUser();
        user.domain = null;
        assertEquals(null, user.getParentDomain());
        user.domain = "";
        assertEquals("", user.getParentDomain());
        user.domain = "abc.intent";
        assertEquals("abc.intent", user.getParentDomain());
        user.domain = "123.homes.abc.intent";
        assertEquals("abc.intent", user.getParentDomain());
        user.domain = ".homes.abc.intent";
        assertEquals("abc.intent", user.getParentDomain());
        user.domain = "123.homes.";
        assertEquals("123.homes.", user.getParentDomain());
    }
}
