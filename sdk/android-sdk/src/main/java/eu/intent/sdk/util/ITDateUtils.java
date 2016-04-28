package eu.intent.sdk.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A set of methods to work with dates.
 */
public class ITDateUtils {
    private static DateFormat dfIso8601 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.US);

    /**
     * Formats the given date to a string in the ISO 8601 format.
     */
    public static String formatDateIso8601(Date date) {
        return dfIso8601.format(date);
    }
}
