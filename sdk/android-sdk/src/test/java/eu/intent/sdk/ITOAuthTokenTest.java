package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.auth.ITOAuthToken;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ITOAuthTokenTest {
    @Test
    public void jsonToToken() {
        String json = TestUtils.INSTANCE.getResourceAsString("oauth_token.json");
        ITOAuthToken token = TestUtils.INSTANCE.getGson().fromJson(json, ITOAuthToken.class);
        assertEquals("The access token is not parsed correctly", "123", token.getAccessToken());
        assertEquals("The refresh token is not parsed correctly", "456", token.getRefreshToken());
        assertEquals("The token type is not parsed correctly", "bearer", token.getTokenType());
        assertEquals("The token expiry is not parsed correctly", 3600, token.getExpiresIn());
        assertEquals("The token expiry is not parsed correctly", 3600000, token.getExpiresInMs());
        assertTrue("The token should expire in the future", token.getExpiry() > System.currentTimeMillis());
    }

    @Test
    public void jsonToTokenNoRefresh() {
        String json = TestUtils.INSTANCE.getResourceAsString("oauth_token_no_refresh.json");
        ITOAuthToken token = TestUtils.INSTANCE.getGson().fromJson(json, ITOAuthToken.class);
        assertEquals("The access token is not parsed correctly", "123", token.getAccessToken());
        assertEquals("The refresh token should be empty", "", token.getRefreshToken());
        assertEquals("The token type is not parsed correctly", "bearer", token.getTokenType());
        assertEquals("The token expiry is not parsed correctly", 3600, token.getExpiresIn());
        assertEquals("The token expiry is not parsed correctly", 3600000, token.getExpiresInMs());
        assertTrue("The token should expire in the future", token.getExpiry() > System.currentTimeMillis());
    }
}
