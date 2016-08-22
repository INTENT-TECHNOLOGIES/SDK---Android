package eu.intent.sdk.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A set of methods to work with dates.
 */
public final class ITDateUtils {
    private static final String ISO_8601_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSZ";

    private ITDateUtils() {
    }

    /**
     * Formats the given date to a string in the ISO 8601 format.
     */
    public static String formatDateIso8601(Date date) {
        return new SimpleDateFormat(ISO_8601_FORMAT, Locale.US).format(date);
    }
}
