package eu.intent.sdktests;

import android.support.annotation.NonNull;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITGenericApi;
import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ITGenericApiTest {
    @Test
    public void testGetUnreachableUrl() {
        new ITGenericApi().get("", new ITApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody result) {
                fail("The URL does not exist, this should fail");
            }

            @Override
            public void onFailure(int httpCode, @NonNull String message, @NonNull String errorBody) {
                assertEquals(-1, httpCode);
            }
        });
    }

    @Test
    public void testGetReachableUrl() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("Hello, world!"));
        server.start();

        HttpUrl url = server.url("/fakeapi");
        new ITGenericApi().get(url.toString(), new ITApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody result) {
                try {
                    assertEquals("Hello, world!", result.string());
                } catch (IOException e) {
                    fail("Fail to read response");
                }
            }

            @Override
            public void onFailure(int httpCode, @NonNull String message, @NonNull String errorBody) {
                fail("Fail to get response");
            }
        });

        server.shutdown();
    }
}
