package eu.intent.sdk;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import eu.intent.sdk.auth.internal.Oauth;
import eu.intent.sdk.util.ITConfig;

/**
 * An Application class with util methods.
 */
public abstract class ITApp extends Application {
    private static final int SESSION_EXPIRED_MESSAGE_DELAY_MS = 5000;

    private ITConfig mConfig;
    private BroadcastReceiver mSessionExpiredBroadcastReceiver;
    private long mLastSessionExpiredMessage;

    /**
     * Returns the current instance of ITApp. The given context must be an instance of Activity or Service.
     *
     * @return the instance of ITApp, or null if the context is not an Activity or a Service, or if the current Application is not an instance of ITApp
     */
    public static ITApp getInstance(Context context) {
        Application app = null;
        context = context.getApplicationContext();
        if (context instanceof Application) {
            app = (Application) context;
        } else if (context instanceof Activity) {
            app = ((Activity) context).getApplication();
        } else if (context instanceof Service) {
            app = ((Service) context).getApplication();
        }
        if (app == null) {
            Log.e(ITApp.class.getCanonicalName(), context.getClass().getSimpleName() + " is not attached to an application instance");
            return null;
        }
        if (app instanceof ITApp) {
            return (ITApp) app;
        }
        Log.e(ITApp.class.getCanonicalName(), "The application must inherit from ITApp (found " + app.getClass().getSimpleName() + ")");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSessionExpiredBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sessionExpired();
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mSessionExpiredBroadcastReceiver, new IntentFilter(Oauth.ACTION_SESSION_EXPIRED));
    }

    @Override
    public void onTerminate() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSessionExpiredBroadcastReceiver);
        super.onTerminate();
    }

    /**
     * Returns the resource ID of the configuration file used in ITConfig. You must override this method to return the resource ID of your configuration file. This configuration file must be in /res/raw.
     */
    public abstract int getConfigFileResId();

    /**
     * @return an instance of ITConfig
     */
    public ITConfig getConfig() {
        if (mConfig == null) {
            mConfig = new ITConfig(this, getConfigFileResId());
        }
        return mConfig;
    }

    /**
     * Called when the user session expired. The default behaviour is to show a Toast explaining that the session expired. You can override this method to implement a custom behaviour.
     */
    protected void sessionExpired() {
        if (System.currentTimeMillis() - mLastSessionExpiredMessage > SESSION_EXPIRED_MESSAGE_DELAY_MS) {
            Toast.makeText(this, R.string.it_session_expired, Toast.LENGTH_SHORT).show();
            mLastSessionExpiredMessage = System.currentTimeMillis();
        }
    }
}
