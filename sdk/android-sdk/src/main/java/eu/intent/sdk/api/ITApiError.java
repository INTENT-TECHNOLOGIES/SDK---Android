package eu.intent.sdk.api;

import android.content.Context;

import eu.intent.sdk.R;
import eu.intent.sdk.util.ITConnectivityHelper;

/**
 * An util class to manage errors sent back by the API.
 */
public class ITApiError {
    /**
     * Returns a readable message from the given HTTP code. If no generic message matches the given code, the fallback message is returned. Most of the time, this fallback message should be the response message.
     */
    public static String getHumanReadableMessage(Context context, int httpCode, String fallbackMessage) {
        if (httpCode >= 0) {
            int resId = context.getResources().getIdentifier("it_error_" + httpCode, "string", context.getPackageName());
            if (resId > 0) {
                return context.getString(resId);
            } else {
                return context.getString(R.string.it_error, httpCode, fallbackMessage);
            }
        } else if (!ITConnectivityHelper.isNetworkAvailable(context)) {
            return context.getString(R.string.it_error_no_network, fallbackMessage);
        } else {
            return context.getString(R.string.it_error_internal, fallbackMessage);
        }
    }
}
