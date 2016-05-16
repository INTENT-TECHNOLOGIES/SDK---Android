package eu.intent.sdktests;

import android.content.Context;
import android.text.TextUtils;

import org.mockito.internal.util.io.IOUtil;

public class TestUtils {
    public static String readRawFile(Context context, int resId) {
        return TextUtils.join("\n", IOUtil.readLines(context.getResources().openRawResource(resId)));
    }
}
