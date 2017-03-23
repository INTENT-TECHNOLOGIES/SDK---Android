package eu.intent.sdk;

import org.junit.Test;

import eu.intent.sdk.model.ITSubscription;

import static junit.framework.Assert.assertEquals;

public class ITSubscriptionTest {
    @Test
    public void jsonToSubscriptions() {
        String json = TestUtils.INSTANCE.getResourceAsString("subscriptions.json");
        ITSubscription[] subscriptions = TestUtils.INSTANCE.getGson().fromJson(json, ITSubscription[].class);
        assertEquals(5, subscriptions.length);
        assertEquals(ITSubscription.Category.NEWS, subscriptions[1].category);
        assertEquals(ITSubscription.Category.CLASSIFIED_AD, subscriptions[2].category);
        assertEquals(ITSubscription.Category.CLASSIFIED_AD_REPLY, subscriptions[3].category);
        assertEquals(ITSubscription.Category.CLASSIFIED_AD_EXPIRY, subscriptions[4].category);
        assertEquals(0, subscriptions[0].emails.length);
        assertEquals(2, subscriptions[1].emails.length);
        assertEquals(1, subscriptions[2].emails.length);
        assertEquals(0, subscriptions[3].emails.length);
        assertEquals(0, subscriptions[4].emails.length);
        assertEquals("test@intent.com", subscriptions[1].emails[0]);
        assertEquals(0, subscriptions[0].sms.length);
        assertEquals(0, subscriptions[1].sms.length);
        assertEquals(0, subscriptions[2].sms.length);
        assertEquals(1, subscriptions[3].sms.length);
        assertEquals(2, subscriptions[4].sms.length);
        assertEquals("06 06 06", subscriptions[4].sms[0]);
        assertEquals(0, subscriptions[0].push.length);
        assertEquals(1, subscriptions[1].push.length);
        assertEquals(2, subscriptions[2].push.length);
        assertEquals(2, subscriptions[3].push.length);
        assertEquals(2, subscriptions[4].push.length);
        assertEquals("d8jnfrnYZ7s", subscriptions[4].push[0].deviceId);
        assertEquals("fVs8YqThh4g", subscriptions[4].push[1].deviceId);
        assertEquals(null, subscriptions[4].push[0].locale);
        assertEquals("fr", subscriptions[4].push[1].locale);
        assertEquals(ITSubscription.Platform.ANDROID, subscriptions[4].push[0].platform);
        assertEquals(ITSubscription.Platform.IOS, subscriptions[4].push[1].platform);
        assertEquals("abc", subscriptions[4].push[0].token);
        assertEquals("def", subscriptions[4].push[1].token);
    }
}
