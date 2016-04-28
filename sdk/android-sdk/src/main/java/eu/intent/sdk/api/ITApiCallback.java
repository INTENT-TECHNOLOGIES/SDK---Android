package eu.intent.sdk.api;

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
     * @param httpCode the HTTP status code, or -1 if the host was not reachable
     * @param message  a message explaining the error
     */
    void onFailure(int httpCode, String message);
}
