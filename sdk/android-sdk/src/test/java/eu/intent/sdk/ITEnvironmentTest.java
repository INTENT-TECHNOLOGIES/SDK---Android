package eu.intent.sdk;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import eu.intent.sdk.util.ITEnvironment;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ITEnvironmentTest {
    @Test
    public void parcel() {
        ITEnvironment environmentIn = new ITEnvironment("https://fake.com/auth/", "https://fake.com/api/", "https://fake.com/redirect/");
        Parcel parcel = Parcel.obtain();
        parcel.writeParcelable(environmentIn, 0);
        parcel.setDataPosition(0);
        ITEnvironment environmentOut = parcel.readParcelable(ITEnvironment.class.getClassLoader());
        assertEquals("The output environment should have the same auth URL than the original environment", environmentIn.getAuthBaseUrl(), environmentOut.getAuthBaseUrl());
        assertEquals("The output environment should have the same api URL than the original environment", environmentIn.getApiBaseUrl(), environmentOut.getApiBaseUrl());
        assertEquals("The output environment should have the same redirect URL than the original environment", environmentIn.getAuthRedirectUrl(), environmentOut.getAuthRedirectUrl());
        parcel.recycle();
    }
}
