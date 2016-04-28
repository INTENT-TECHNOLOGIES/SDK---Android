package eu.intent.sdk.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class loads properties from a configuration file, and provides methods to retrieve these properties.
 */
public class ITConfig {
    private Properties mProperties;

    /**
     * Creates an instance of ITConfig from a configuration file. The configuration file must be in /res/raw.
     */
    public ITConfig(Context context, int configFileId) {
        mProperties = new Properties();
        if (configFileId > 0) {
            try {
                InputStream rawResource = context.getResources().openRawResource(configFileId);
                mProperties.load(rawResource);
            } catch (Resources.NotFoundException e) {
                Log.e(getClass().getCanonicalName(), "Config file not found", e);
            } catch (IOException e) {
                Log.e(getClass().getCanonicalName(), "Could not load config file", e);
            }
        } else {
            Log.w(getClass().getCanonicalName(), "No config file provided");
        }
    }

    /**
     * @return a String value from a property key, or en empty string if the property does not exist
     */
    public String getString(String key) {
        return getString(key, "");
    }

    /**
     * @return a String value from a property key, or the default value if the property does not exist
     */
    public String getString(String key, String defaultValue) {
        return mProperties.getProperty(key, defaultValue);
    }

    /**
     * @return a boolean value from a property key, or false if the property does not exist
     */
    public boolean getBool(String key) {
        return getBool(key, false);
    }

    /**
     * @return a boolean value from a property key, or the default value if the property does not exist
     */
    public boolean getBool(String key, boolean defaultValue) {
        return Boolean.parseBoolean(mProperties.getProperty(key, String.valueOf(defaultValue)));
    }

    /**
     * @return a float value from a property key, or 0 if the property does not exist
     */
    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    /**
     * @return a float value from a property key, or the default value if the property does not exist
     */
    public float getFloat(String key, float defaultValue) {
        return Float.parseFloat(mProperties.getProperty(key, String.valueOf(defaultValue)));
    }

    /**
     * @return an int value from a property key, or 0 if the property does not exist
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * @return an int value from a property key, or the default value if the property does not exist
     */
    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(mProperties.getProperty(key, String.valueOf(defaultValue)));
    }
}
