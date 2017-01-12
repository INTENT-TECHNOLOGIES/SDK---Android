package eu.intent.sdk.api.internal;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import eu.intent.sdk.api.ITApiCallback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A callback that handles the response and dispatch it to the final success or failure callback method.
 */
public class ProxyCallback<T> implements Callback<T> {
    private ITApiCallback<T> mCallback;

    public ProxyCallback(ITApiCallback<T> callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            if (mCallback != null) mCallback.onSuccess(response.body());
        } else {
            String message = response.message();
            if (message == null) message = "";
            ResponseBody errorBody = response.errorBody();
            String errorBodyString = "";
            if (errorBody != null) {
                try {
                    errorBodyString = errorBody.string();
                } catch (IOException ignored) {
                    // errorBodyString will be empty
                }
            }
            Log.e(getClass().getCanonicalName(), TextUtils.join(" ", Arrays.asList(message, errorBodyString)));
            if (mCallback != null)
                mCallback.onFailure(response.code(), message, errorBodyString);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.w(getClass().getCanonicalName(), "Request failed", t);
        String message = t.getLocalizedMessage();
        if (mCallback != null) mCallback.onFailure(-1, message == null ? "" : message, "");
    }
}
