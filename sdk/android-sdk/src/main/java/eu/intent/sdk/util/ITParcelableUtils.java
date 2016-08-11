package eu.intent.sdk.util;

import android.os.Parcel;
import android.support.annotation.NonNull;

/**
 * Provides some util methods to work with Parcelable classes.
 */
public final class ITParcelableUtils {
    private ITParcelableUtils() {
    }

    public static void writeBoolean(@NonNull Parcel dest, boolean value) {
        if (value) {
            dest.writeByte((byte) 1);
        } else {
            dest.writeByte((byte) 0);
        }
    }

    public static boolean readBoolean(@NonNull Parcel in) {
        return in.readByte() > 0;
    }
}
