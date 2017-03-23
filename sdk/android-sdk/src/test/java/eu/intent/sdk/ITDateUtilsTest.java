package eu.intent.sdk;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import eu.intent.sdk.util.ITDateUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class ITDateUtilsTest {
    @Test
    public void formatDateZero() {
        Date date = new Date(0);
        String string = ITDateUtils.formatDateIso8601(date);
        assertEquals("Date should be correctly formatted", "1970-01-01T00:00:00.000+0000", string);
    }

    @Test
    public void formatGmt2() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
        calendar.set(2017, Calendar.JANUARY, 1, 1, 15, 30);
        calendar.set(Calendar.MILLISECOND, 0);
        String string = ITDateUtils.formatDateIso8601(calendar.getTime());
        assertEquals("Date should be formatted in GMT", "2016-12-31T23:15:30.000+0000", string);
    }

    @Test(expected = ParseException.class)
    public void parseMalformedString() throws ParseException {
        String string = "2017-04-05 12:15:30";
        ITDateUtils.parseDateIso8601(string);
    }

    @Test
    public void parseGmt() {
        String string = "2017-04-05T12:15:30.123Z";
        try {
            Date date = ITDateUtils.parseDateIso8601(string);
            Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
            expected.set(2017, Calendar.APRIL, 5, 12, 15, 30);
            expected.set(Calendar.MILLISECOND, 123);
            assertEquals("GMT string should be correctly parsed", expected.getTime(), date);
        } catch (ParseException ignored) {
            fail("Year-Month-Day should not throw an exception");
        }
    }

    @Test
    public void parseGmt2() {
        String string = "2017-04-05T12:15:30.123+0200";
        try {
            Date date = ITDateUtils.parseDateIso8601(string);
            Calendar expected = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
            expected.set(2017, Calendar.APRIL, 5, 12, 15, 30);
            expected.set(Calendar.MILLISECOND, 123);
            assertEquals("GMT+2 string should be correctly parsed", expected.getTime(), date);
        } catch (ParseException ignored) {
            fail("Year-Month-Day should not throw an exception");
        }
    }
}
