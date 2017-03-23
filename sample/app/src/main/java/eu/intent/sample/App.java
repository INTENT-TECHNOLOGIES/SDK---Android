package eu.intent.sample;

import android.app.Application;

import eu.intent.sdk.auth.ITClient;
import eu.intent.sdk.auth.ITSessionManager;
import eu.intent.sdk.network.ITRetrofitHelper;
import eu.intent.sdk.util.ITEnvironment;
import retrofit2.Retrofit;

public class App extends Application {
    public static App INSTANCE;

    private ITClient mApiClient;
    private Retrofit mRetrofit;
    private ITSessionManager mSessionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public ITClient getApiClient() {
        if (mApiClient == null) {
            mApiClient = new ITClient(
                    ITEnvironment.SANDBOX,
                    "your_client_id",
                    "your_client_secret",
                    "your_redirect_uri",
                    "read:me", "read:datahub:parts");
        }
        return mApiClient;
    }

    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = ITRetrofitHelper.INSTANCE.getDefaultApiRetrofit(this, getApiClient());
        }
        return mRetrofit;
    }

    public ITSessionManager getSessionManager() {
        if (mSessionManager == null) {
            mSessionManager = new ITSessionManager(this);
        }
        return mSessionManager;
    }
}
