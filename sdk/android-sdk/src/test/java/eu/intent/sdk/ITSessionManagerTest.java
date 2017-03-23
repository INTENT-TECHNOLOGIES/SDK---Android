package eu.intent.sdk;


import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import eu.intent.sdk.auth.ITOAuthToken;
import eu.intent.sdk.auth.ITSessionManager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ITSessionManagerTest {
    private final ITSessionManager mSessionManager = new ITSessionManager(RuntimeEnvironment.application);

    @After
    public void cleanup() {
        mSessionManager.logout();
    }

    @Test
    public void noLogin() {
        assertFalse("There should be no session by default", mSessionManager.isLoggedIn());
        assertEquals("There should be no access token by default", "", mSessionManager.getAccessToken());
        assertEquals("There should be no access token expiry by default", 0, mSessionManager.getAccessTokenExpiry());
        assertEquals("There should be no refresh token by default", "", mSessionManager.getRefreshToken());
        assertFalse("There should be no refresh token by default", mSessionManager.canRefreshToken());
    }

    @Test
    public void sessionRunning() {
        ITOAuthToken token = new ITOAuthToken(10000, "access", "", "bearer");
        mSessionManager.login(token);
        assertTrue("The session should be running", mSessionManager.isLoggedIn());
        assertEquals("There should be an access token", "access", mSessionManager.getAccessToken());
        assertTrue("The access token should expire in the future", mSessionManager.getAccessTokenExpiry() > System.currentTimeMillis());
    }

    @Test
    public void sessionExpiredCanRefresh() {
        ITOAuthToken token = new ITOAuthToken(-10000, "access", "refresh", "bearer");
        mSessionManager.login(token);
        assertFalse("The session should be expired", mSessionManager.isLoggedIn());
        assertEquals("There should be an access token", "access", mSessionManager.getAccessToken());
        assertTrue("The access token should be expired", mSessionManager.getAccessTokenExpiry() < System.currentTimeMillis());
        assertEquals("There should be a refresh token", "refresh", mSessionManager.getRefreshToken());
        assertTrue("The session should allow to refresh the token", mSessionManager.canRefreshToken());
    }

    @Test
    public void sessionExpiredCanNotRefresh() {
        ITOAuthToken token = new ITOAuthToken(-10000, "access", "", "bearer");
        mSessionManager.login(token);
        assertFalse("The session should be expired", mSessionManager.isLoggedIn());
        assertEquals("There should be an access token", "access", mSessionManager.getAccessToken());
        assertTrue("The access token should be expired", mSessionManager.getAccessTokenExpiry() < System.currentTimeMillis());
        assertEquals("There should be no refresh token", "", mSessionManager.getRefreshToken());
        assertFalse("The session should not allow to refresh the token", mSessionManager.canRefreshToken());
    }

    @Test
    public void logout() {
        ITOAuthToken token = new ITOAuthToken(10000, "access", "refresh", "bearer");
        mSessionManager.login(token);
        assertTrue("The session should be running", mSessionManager.isLoggedIn());
        assertEquals("There should be an access token", "access", mSessionManager.getAccessToken());
        assertTrue("The access token should expire in the future", mSessionManager.getAccessTokenExpiry() > System.currentTimeMillis());
        assertEquals("There should be a refresh token", "refresh", mSessionManager.getRefreshToken());
        assertTrue("The session should allow to refresh the token", mSessionManager.canRefreshToken());
        mSessionManager.logout();
        assertFalse("The session should be expired", mSessionManager.isLoggedIn());
        assertEquals("There should be no access token", "", mSessionManager.getAccessToken());
        assertEquals("There should be no access token expiry", 0, mSessionManager.getAccessTokenExpiry());
        assertEquals("There should be no refresh token", "", mSessionManager.getRefreshToken());
        assertFalse("The session should not allow to refresh the token", mSessionManager.canRefreshToken());
    }
}
