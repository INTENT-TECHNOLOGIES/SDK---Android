package eu.intent.sdk.api.internal;

import android.util.Log;

import java.io.IOException;

import eu.intent.sdk.api.ITApiCallback;
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
            try {
                Log.e(getClass().getCanonicalName(), response.errorBody().string());
            } catch (IOException ignored) {
                Log.e(getClass().getCanonicalName(), response.message() != null ? response.message() : "");
            }
            if (mCallback != null)
                mCallback.onFailure(response.code(), response.message() != null ? response.message() : "");
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.w(getClass().getCanonicalName(), "Request failed", t);
        if (mCallback != null) mCallback.onFailure(-1, t.getLocalizedMessage());
    }
}
