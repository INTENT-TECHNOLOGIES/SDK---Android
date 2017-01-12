package eu.intent.sdk.api;

import android.support.annotation.NonNull;

/**
 * Implement this interface to handle the response or failure of the Intent API.
 */
public interface ITApiCallback<T> {
    /**
     * Called on request success.
     *
     * @param result the result converted into T class
     */
    void onSuccess(T result);

    /**
     * Called on request failure.
     *
     * @param httpCode  the HTTP status code, or -1 if the host was not reachable
     * @param message   a message explaining the error
     * @param errorBody the error body if provided, converted to String
     */
    void onFailure(int httpCode, @NonNull String message, @NonNull String errorBody);
}
