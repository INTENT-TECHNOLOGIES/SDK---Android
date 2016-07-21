package eu.intent.sdk.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * This class defines a set of thresholds, defined by a min/max value and a time window.
 * The threshold values can be min and/or max.
 * The time window is optional, and can be defined by a start and/or an end hour/minute. It can be set on a given day (from 0 to 6), or all the days of week (-1).
 *
 * @see ITState
 * @see ITStateParams
 */
public class ITStateParamsThresholds implements ITStateParams, Parcelable {
    public static final Parcelable.Creator<ITStateParamsThresholds> CREATOR = new Parcelable.Creator<ITStateParamsThresholds>() {
        public ITStateParamsThresholds createFromParcel(Parcel source) {
            return new ITStateParamsThresholds(source);
        }

        public ITStateParamsThresholds[] newArray(int size) {
            return new ITStateParamsThresholds[size];
        }
    };

    @SerializedName("timetables")
    public Threshold[] thresholds;

    /**
     * You can put whatever you want in this bundle, for example add properties to this object in order to use it in an adapter.
     * WARNING! Custom classes will not be saved when generating a Parcelable from this object.
     */
    transient public Bundle custom = new Bundle();

    public ITStateParamsThresholds() {
    }

    protected ITStateParamsThresholds(Parcel in) {
        Parcelable[] parcelables = in.readParcelableArray(Threshold.class.getClassLoader());
        if (parcelables != null) {
            thresholds = new Threshold[parcelables.length];
            for (int i = 0; i < parcelables.length; i++) {
                thresholds[i] = (Threshold) parcelables[i];
            }
        } else {
            thresholds = new Threshold[0];
        }
        custom = in.readBundle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(thresholds, flags);
        dest.writeBundle(custom);
    }

    /**
     * Returns the Threshold operative at the given time. Returns null if there is no such Threshold. If several Thresholds are found, returns the first one.
     */
    public Threshold getThresholdAt(long time) {
        for (Threshold threshold : thresholds) {
            if (threshold.isInEffectAt(time)) {
                return threshold;
            }
        }
        return null;
    }

    public static class Threshold implements Parcelable {
        public static final Creator<Threshold> CREATOR = new Creator<Threshold>() {
            public Threshold createFromParcel(Parcel source) {
                return new Threshold(source);
            }

            public Threshold[] newArray(int size) {
                return new Threshold[size];
            }
        };

        public double min = Double.NaN;
        public double max = Double.NaN;
        public int fromHour = -1;
        public int toHour = -1;
        public int fromMinute = 0;
        public int toMinute = 0;
        public int dayOfWeek = -1;  // 0 to 6, 0 is Sunday (add 1 for Java)

        public Threshold() {
        }

        protected Threshold(Parcel in) {
            min = in.readDouble();
            max = in.readDouble();
            fromHour = in.readInt();
            toHour = in.readInt();
            fromMinute = in.readInt();
            toMinute = in.readInt();
            dayOfWeek = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(min);
            dest.writeDouble(max);
            dest.writeInt(fromHour);
            dest.writeInt(toHour);
            dest.writeInt(fromMinute);
            dest.writeInt(toMinute);
            dest.writeInt(dayOfWeek);
        }

        /**
         * Returns true if this threshold is operative at the given time, false otherwise.
         */
        public boolean isInEffectAt(long time) {
            boolean result = false;
            // The time tables are stored in a specific time zone (French time)
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
            c.setTimeInMillis(time);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            if (fromHour >= 0 && toHour >= 0) {
                // (Exclude the end of the timetable)
                if (isAcrossTwoDays()) {
                    if (dayOfWeek < 0) {
                        // No day: we can be either after fromHour/fromMinute, or before toHour/toMinute
                        result = (hour > fromHour || hour == fromHour && minute >= fromMinute)
                                || (hour < toHour || hour == toHour && minute < toMinute);
                    } else if (c.get(Calendar.DAY_OF_WEEK) - 1 == dayOfWeek) {
                        // D-day: we must be after fromHour/fromMinute
                        result = hour > fromHour || hour == fromHour && minute >= fromMinute;
                    } else if (c.get(Calendar.DAY_OF_WEEK) - 1 == ((dayOfWeek + 1) % 7)) {
                        // D+1: we must be before toHour/toMinute
                        result = hour < toHour || hour == toHour && minute < toMinute;
                    }
                } else {
                    result = (dayOfWeek < 0 || c.get(Calendar.DAY_OF_WEEK) - 1 == dayOfWeek)
                            && (hour > fromHour || hour == fromHour && minute >= fromMinute)
                            && (hour < toHour || hour == toHour && minute < toMinute);
                }
            } else {
                result = dayOfWeek < 0 || c.get(Calendar.DAY_OF_WEEK) - 1 == dayOfWeek;
            }
            return result;
        }

        /**
         * Returns true if this threshold is defined across two days, ie its end time is greater than its start time.
         */
        public boolean isAcrossTwoDays() {
            return fromHour > toHour || fromHour == toHour && fromMinute > toMinute;
        }
    }
}
