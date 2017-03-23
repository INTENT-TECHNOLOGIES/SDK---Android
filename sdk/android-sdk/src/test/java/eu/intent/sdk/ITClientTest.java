package eu.intent.sdk;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import eu.intent.sdk.auth.ITClient;
import eu.intent.sdk.util.ITEnvironment;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ITClientTest {
    private final ITEnvironment mEnvironment = new ITEnvironment("https://fake.com/auth/", "https://fake.com/api/", "https://fake.com/code/");

    @Test
    public void webFormUrlNoScope() {
        ITClient client = new ITClient(mEnvironment, "clientId", "clientSecret", "redirectUri");
        String url = client.getWebFormUrl();
        assertEquals("Web form URL should be correct", "https://fake.com/auth/authorize?response_type=code&client_id=clientId&redirect_uri=redirectUri", url);
    }

    @Test
    public void webFormUrlOneScope() {
        ITClient client = new ITClient(mEnvironment, "clientId", "clientSecret", "redirectUri", "scope1");
        String url = client.getWebFormUrl();
        assertEquals("Web form URL should be correct", "https://fake.com/auth/authorize?response_type=code&client_id=clientId&redirect_uri=redirectUri&scope=scope1", url);
    }

    @Test
    public void webFormUrlTwoScopes() {
        ITClient client = new ITClient(mEnvironment, "clientId", "clientSecret", "redirectUri", "scope1", "scope2");
        String url = client.getWebFormUrl();
        assertEquals("Web form URL should be correct", "https://fake.com/auth/authorize?response_type=code&client_id=clientId&redirect_uri=redirectUri&scope=scope1%20scope2", url);
    }

    @Test
    public void parcelWithScopes() {
        ITClient clientIn = new ITClient(mEnvironment, "clientId", "clientSecret", "redirectUri", "scope1", "scope2");
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(clientIn, 0);
        parcel.setDataPosition(0);
        ITClient clientOut = parcel.readParcelable(ITClient.class.getClassLoader());
        assertEquals("The output client should have the same environment than the original client", clientIn.getEnvironment(), clientOut.getEnvironment());
        assertEquals("The output client should have the same client ID than the original client", clientIn.getClientId(), clientOut.getClientId());
        assertEquals("The output client should have the same client secret than the original client", clientIn.getClientSecret(), clientOut.getClientSecret());
        assertEquals("The output client should have the same redirect URI than the original client", clientIn.getRedirectUri(), clientOut.getRedirectUri());
        assertEquals("The output client should have the same web form URL than the original client", clientIn.getWebFormUrl(), clientOut.getWebFormUrl());
        assertEquals("The output client should have the same scopes than the original client", 2, clientIn.getScopes().length);
        assertEquals("The output client should have the same scopes than the original client", clientIn.getScopes()[0], clientOut.getScopes()[0]);
        assertEquals("The output client should have the same scopes than the original client", clientIn.getScopes()[1], clientOut.getScopes()[1]);
        parcel.recycle();
    }

    @Test
    public void parcelWithoutScopes() {
        ITClient clientIn = new ITClient(mEnvironment, "clientId", "clientSecret", "redirectUri");
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(clientIn, 0);
        parcel.setDataPosition(0);
        ITClient clientOut = parcel.readParcelable(ITClient.class.getClassLoader());
        assertEquals("The output client should have the same environment than the original client", clientIn.getEnvironment(), clientOut.getEnvironment());
        assertEquals("The output client should have the same client ID than the original client", clientIn.getClientId(), clientOut.getClientId());
        assertEquals("The output client should have the same client secret than the original client", clientIn.getClientSecret(), clientOut.getClientSecret());
        assertEquals("The output client should have the same redirect URI than the original client", clientIn.getRedirectUri(), clientOut.getRedirectUri());
        assertEquals("The output client should have the same web form URL than the original client", clientIn.getWebFormUrl(), clientOut.getWebFormUrl());
        assertEquals("The output client should have an empty scopes array", 0, clientOut.getScopes().length);
        parcel.recycle();
    }
}
