package eu.intent.sdk;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import eu.intent.sdk.util.ITParcelableUtils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ITParcelableUtilsTest {
    @Test
    public void falseToZero() {
        Parcel parcel = Parcel.obtain();
        ITParcelableUtils.writeBoolean(parcel, false);
        parcel.setDataPosition(0);
        assertEquals("False should write 0 in Parcel", 0, parcel.readByte());
        parcel.recycle();
    }

    @Test
    public void trueToOne() {
        Parcel parcel = Parcel.obtain();
        ITParcelableUtils.writeBoolean(parcel, true);
        parcel.setDataPosition(0);
        assertEquals("True should write 1 in Parcel", 1, parcel.readByte());
        parcel.recycle();
    }

    @Test
    public void zeroToFalse() {
        Parcel parcel = Parcel.obtain();
        parcel.writeByte((byte) 0);
        parcel.setDataPosition(0);
        assertFalse("Parcel should read false from 0", ITParcelableUtils.readBoolean(parcel));
        parcel.recycle();
    }

    @Test
    public void negativeToFalse() {
        Parcel parcel = Parcel.obtain();
        parcel.writeByte((byte) -1);
        parcel.setDataPosition(0);
        assertFalse("Parcel should read false from negative value", ITParcelableUtils.readBoolean(parcel));
        parcel.recycle();
    }

    @Test
    public void oneToTrue() {
        Parcel parcel = Parcel.obtain();
        parcel.writeByte((byte) 1);
        parcel.setDataPosition(0);
        assertTrue("Parcel should read true from 1", ITParcelableUtils.readBoolean(parcel));
        parcel.recycle();
    }
}
