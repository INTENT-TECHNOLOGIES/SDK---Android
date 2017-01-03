package eu.intent.sdk.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A set of methods to work with dates.
 */
public final class ITDateUtils {
    private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private ITDateUtils() {
    }

    /**
     * Formats the given date to a string in the ISO 8601 format.
     */
    public static String formatDateIso8601(Date date) {
        return getIso8601DateFormat().format(date);
    }

    /**
     * Parses the given date in ISO 8601 format to a Date.
     *
     * @throws ParseException if the given date is not in the expected format
     */
    public static Date parseDateIso8601(String date) throws ParseException {
        return getIso8601DateFormat().parse(date.replaceAll("Z$", "+0000"));
    }

    private static DateFormat getIso8601DateFormat() {
        DateFormat dateFormat = new SimpleDateFormat(ISO_8601_FORMAT, Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat;
    }
}
