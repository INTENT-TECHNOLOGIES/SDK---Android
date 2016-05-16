package eu.intent.sdktests;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import eu.intent.sdk.api.ITRetrofitUtils;
import eu.intent.sdk.model.ITSubscription;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ITSubscriptionTest {
    @Test
    public void jsonToSubscriptions() {
        String json = TestUtils.readRawFile(InstrumentationRegistry.getContext(), eu.intent.sdktests.test.R.raw.subscriptions);
        Type listType = new TypeToken<List<ITSubscription>>() {
        }.getType();
        List<ITSubscription> subscriptions = ITRetrofitUtils.getGson().fromJson(json, listType);
        assertEquals(5, subscriptions.size());
        assertEquals(ITSubscription.Category.NEWS, subscriptions.get(1).category);
        assertEquals(ITSubscription.Category.CLASSIFIED_AD, subscriptions.get(2).category);
        assertEquals(ITSubscription.Category.CLASSIFIED_AD_REPLY, subscriptions.get(3).category);
        assertEquals(ITSubscription.Category.CLASSIFIED_AD_EXPIRY, subscriptions.get(4).category);
        assertEquals(0, subscriptions.get(0).emails.length);
        assertEquals(2, subscriptions.get(1).emails.length);
        assertEquals(1, subscriptions.get(2).emails.length);
        assertEquals(0, subscriptions.get(3).emails.length);
        assertEquals(0, subscriptions.get(4).emails.length);
        assertEquals("test@intent.com", subscriptions.get(1).emails[0]);
        assertEquals(0, subscriptions.get(0).sms.length);
        assertEquals(0, subscriptions.get(1).sms.length);
        assertEquals(0, subscriptions.get(2).sms.length);
        assertEquals(1, subscriptions.get(3).sms.length);
        assertEquals(2, subscriptions.get(4).sms.length);
        assertEquals("06 06 06", subscriptions.get(4).sms[0]);
        assertEquals(0, subscriptions.get(0).push.length);
        assertEquals(1, subscriptions.get(1).push.length);
        assertEquals(2, subscriptions.get(2).push.length);
        assertEquals(2, subscriptions.get(3).push.length);
        assertEquals(2, subscriptions.get(4).push.length);
        assertEquals("d8jnfrnYZ7s", subscriptions.get(4).push[0].deviceId);
        assertEquals("fVs8YqThh4g", subscriptions.get(4).push[1].deviceId);
        assertEquals(null, subscriptions.get(4).push[0].locale);
        assertEquals("fr", subscriptions.get(4).push[1].locale);
        assertEquals(ITSubscription.Platform.ANDROID, subscriptions.get(4).push[0].platform);
        assertEquals(ITSubscription.Platform.IOS, subscriptions.get(4).push[1].platform);
        assertEquals("abc", subscriptions.get(4).push[0].token);
        assertEquals("def", subscriptions.get(4).push[1].token);
    }
}
