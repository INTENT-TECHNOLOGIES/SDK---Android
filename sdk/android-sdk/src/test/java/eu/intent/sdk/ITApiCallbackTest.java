package eu.intent.sdk;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import eu.intent.sdk.api.ITApiCallback;
import eu.intent.sdk.api.ITApiError;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class ITApiCallbackTest {
    private final Request mRequest = new Request.Builder().url("https://fake.com/api/").build();
    private final okhttp3.Response mErrorResponseWithMessage = new okhttp3.Response.Builder()
            .request(mRequest).protocol(Protocol.HTTP_1_1).code(400).message("Bad Request").build();
    private final okhttp3.Response mErrorResponseWithoutMessage = new okhttp3.Response.Builder()
            .request(mRequest).protocol(Protocol.HTTP_1_1).code(400).build();

    @Test
    public void failure() {
        ITApiError expectedError = new ITApiError(-1, "", "", "", "Error");
        TestCallback callback = new TestCallback(expectedError);
        callback.onFailure(null, new Throwable("Error"));
    }

    @Test
    public void nullResponse() {
        ITApiError expectedError = new ITApiError(-1, "", "", "", "");
        TestCallback callback = new TestCallback(expectedError);
        callback.onResponse(null, null);
    }

    @Test
    public void errorResponseWithoutHttpMessage() {
        ITApiError expectedError = new ITApiError(400, "", "123", "", "Error");
        TestCallback callback = new TestCallback(expectedError);
        ResponseBody errorBody = ResponseBody.create(
                MediaType.parse("application/json"),
                "{\"code\":\"123\", \"message\":\"Error\"}"
        );
        callback.onResponse(null, Response.<String>error(errorBody, mErrorResponseWithoutMessage));
    }

    @Test
    public void errorResponseWithMessage() {
        ITApiError expectedError = new ITApiError(400, "Bad Request", "123", "Error details", "Error");
        TestCallback callback = new TestCallback(expectedError);
        ResponseBody errorBody = ResponseBody.create(
                MediaType.parse("application/json"),
                "{\"code\":\"123\", \"details\":\"Error details\", \"message\":\"Error\"}"
        );
        callback.onResponse(null, Response.<String>error(errorBody, mErrorResponseWithMessage));
    }

    @Test
    public void errorResponseWithDescription() {
        ITApiError expectedError = new ITApiError(400, "Bad Request", "123", "Error details", "Error");
        TestCallback callback = new TestCallback(expectedError);
        ResponseBody errorBody = ResponseBody.create(
                MediaType.parse("application/json"),
                "{\"code\":\"123\", \"details\":\"Error details\", \"error_description\":\"Error\"}"
        );
        callback.onResponse(null, Response.<String>error(errorBody, mErrorResponseWithMessage));
    }

    @Test
    public void errorResponseWithCodeNumber() {
        ITApiError expectedError = new ITApiError(400, "Bad Request", "123", "Error details", "Error");
        TestCallback callback = new TestCallback(expectedError);
        ResponseBody errorBody = ResponseBody.create(
                MediaType.parse("application/json"),
                "{\"code\":123, \"details\":\"Error details\", \"message\":\"Error\"}"
        );
        callback.onResponse(null, Response.<String>error(errorBody, mErrorResponseWithMessage));
    }

    @Test
    public void malformedErrorResponse() {
        ITApiError expectedError = new ITApiError(400, "Bad Request", "", "", "");
        TestCallback callback = new TestCallback(expectedError);
        ResponseBody errorBody = ResponseBody.create(
                MediaType.parse("application/json"),
                "<html></html>"
        );
        callback.onResponse(null, Response.<String>error(errorBody, mErrorResponseWithMessage));
    }

    @Test
    public void successResponse() {
        String expectedResult = "This is a success";
        TestCallback callback = new TestCallback(expectedResult);
        Response<String> response = Response.success(expectedResult);
        callback.onResponse(null, response);
    }

    private static class TestCallback extends ITApiCallback<String> {
        private boolean mExpectedSuccess;
        private String mExpectedResult;
        private ITApiError mExpectedError;

        TestCallback(String expectedResult) {
            super();
            mExpectedSuccess = true;
            mExpectedResult = expectedResult;
        }

        TestCallback(ITApiError expectedError) {
            super();
            mExpectedSuccess = false;
            mExpectedError = expectedError;
        }

        @Override
        public void onFailure(@Nullable Call<String> call, @NotNull ITApiError error) {
            if (mExpectedSuccess) {
                fail("onFailure should not be called");
            } else {
                assertEquals("Error code is not as expected", mExpectedError.getCode(), error.getCode());
                assertEquals("Error details are not as expected", mExpectedError.getDetails(), error.getDetails());
                assertEquals("Error HTTP code is not as expected", mExpectedError.getHttpCode(), error.getHttpCode());
                assertEquals("Error HTTP message is not as expected", mExpectedError.getHttpMessage(), error.getHttpMessage());
                assertEquals("Error message is not as expected", mExpectedError.getMessage(), error.getMessage());
            }
        }

        @Override
        public void onSuccess(@Nullable Call<String> call, String result) {
            if (mExpectedSuccess) {
                assertEquals("Result is not as expected", mExpectedResult, result);
            } else {
                fail("onSuccess should not be called");
            }
        }
    }
}
