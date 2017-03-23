package eu.intent.sdk;

import android.app.Activity;
import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import eu.intent.sdk.auth.ITAuthHelper;
import eu.intent.sdk.auth.ITClient;
import eu.intent.sdk.ui.activity.ITAuthActivity;
import eu.intent.sdk.util.ITEnvironment;

import static junit.framework.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ITAuthHelperTest {
    private final ITEnvironment mEnvironment = new ITEnvironment("https://fake.com/auth/", "https://fake.com/api/", "https://fake.com/redirect/");
    private final ITClient mClient = new ITClient(mEnvironment, "clientId", "clientSecret", "redirectUri", "scope1", "scope2");

    @Test
    public void loginIntent() {
        Intent intent = ITAuthHelper.INSTANCE.createLoginActivityIntent(RuntimeEnvironment.application, mClient);
        assertEquals("Target class should be ITAuthActivity", ITAuthActivity.class.getName(), intent.getComponent().getClassName());
        ITClient extraClient = intent.getParcelableExtra(ITAuthActivity.EXTRA_CLIENT);
        assertEquals("Extra client should have the same environment than original client", mEnvironment, extraClient.getEnvironment());
        assertEquals("Extra client should have the same client ID than original client", mClient.getClientId(), extraClient.getClientId());
        assertEquals("Extra client should have the same client secret than original client", mClient.getClientSecret(), extraClient.getClientSecret());
        assertEquals("Extra client should have the same redirect URI than original client", mClient.getRedirectUri(), extraClient.getRedirectUri());
        assertEquals("Extra client should have the same scopes than original client", mClient.getScopes().length, extraClient.getScopes().length);
        assertEquals("Extra client should have the same scopes than original client", mClient.getScopes()[0], extraClient.getScopes()[0]);
        assertEquals("Extra client should have the same scopes than original client", mClient.getScopes()[1], extraClient.getScopes()[1]);
        assertEquals("Extra client should have the same web form URL than original client", mClient.getWebFormUrl(), extraClient.getWebFormUrl());
    }

    @Test
    public void loginActivity() {
        Activity someActivity = Robolectric.setupActivity(Activity.class);
        ShadowActivity shadowActivity = shadowOf(someActivity);
        ITAuthHelper.INSTANCE.openLoginActivity(someActivity, mClient, 123);
        ShadowActivity.IntentForResult intent = shadowActivity.getNextStartedActivityForResult();
        assertEquals("Intent request code should be equal to the request code parameter", 123, intent.requestCode);
        ITClient extraClient = intent.intent.getParcelableExtra(ITAuthActivity.EXTRA_CLIENT);
        assertEquals("Extra client should have the same environment than original client", mEnvironment, extraClient.getEnvironment());
        assertEquals("Extra client should have the same client ID than original client", mClient.getClientId(), extraClient.getClientId());
        assertEquals("Extra client should have the same client secret than original client", mClient.getClientSecret(), extraClient.getClientSecret());
        assertEquals("Extra client should have the same redirect URI than original client", mClient.getRedirectUri(), extraClient.getRedirectUri());
        assertEquals("Extra client should have the same scopes than original client", mClient.getScopes().length, extraClient.getScopes().length);
        assertEquals("Extra client should have the same scopes than original client", mClient.getScopes()[0], extraClient.getScopes()[0]);
        assertEquals("Extra client should have the same scopes than original client", mClient.getScopes()[1], extraClient.getScopes()[1]);
        assertEquals("Extra client should have the same web form URL than original client", mClient.getWebFormUrl(), extraClient.getWebFormUrl());
    }
}
