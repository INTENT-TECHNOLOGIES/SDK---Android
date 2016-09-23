package eu.intent.sdk.api;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import eu.intent.sdk.ITApp;
import eu.intent.sdk.api.internal.RetrofitAuthenticator;
import eu.intent.sdk.api.internal.RetrofitCacheInterceptor;
import eu.intent.sdk.api.internal.RetrofitGzipInterceptor;
import eu.intent.sdk.api.internal.RetrofitHeadersInterceptor;
import eu.intent.sdk.model.ITAction;
import eu.intent.sdk.model.ITActivity;
import eu.intent.sdk.model.ITClassifiedAd;
import eu.intent.sdk.model.ITClassifiedAdCategory;
import eu.intent.sdk.model.ITContact;
import eu.intent.sdk.model.ITConversation;
import eu.intent.sdk.model.ITData;
import eu.intent.sdk.model.ITDeviceType;
import eu.intent.sdk.model.ITEquipment;
import eu.intent.sdk.model.ITGreenGesture;
import eu.intent.sdk.model.ITLocation;
import eu.intent.sdk.model.ITMessage;
import eu.intent.sdk.model.ITMessagesCount;
import eu.intent.sdk.model.ITNews;
import eu.intent.sdk.model.ITState;
import eu.intent.sdk.model.ITStateTemplate;
import eu.intent.sdk.model.ITStream;
import eu.intent.sdk.model.ITTag;
import eu.intent.sdk.model.ITTagList;
import eu.intent.sdk.model.ITTask;
import eu.intent.sdk.model.ITUser;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ITRetrofitUtils {
    private static final String CONFIG_BASE_URL = "it_api_base_url";
    private static final String DEFAULT_BASE_URL = "https://api.hubintent.com/api/";

    private static Retrofit sRetrofit;
    private static GsonBuilder sGsonBuilder;
    private static Gson sGson;
    private static Map<Type, Object> sCustomTypeAdapters;

    private ITRetrofitUtils() {
    }

    /**
     * Gets an instance of Retrofit, provided with an authenticator and the headers needed for each request.
     */
    public static Retrofit getRetrofitInstance(Context context) {
        synchronized (ITRetrofitUtils.class) {
            if (sRetrofit == null) {
                OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
                clientBuilder.addInterceptor(new RetrofitCacheInterceptor(context));
                clientBuilder.addInterceptor(new RetrofitGzipInterceptor());
                // TODO: Remove logs before releasing
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilder.addInterceptor(loggingInterceptor);
                clientBuilder.addNetworkInterceptor(new RetrofitHeadersInterceptor(context));
                clientBuilder.authenticator(new RetrofitAuthenticator(context));
                clientBuilder.cache(new Cache(context.getCacheDir(), 1024 * 1024 * 10));
                clientBuilder.connectTimeout(5, TimeUnit.SECONDS);
                clientBuilder.readTimeout(10, TimeUnit.SECONDS);
                sRetrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(getGson())).baseUrl(getBaseUrl(context)).client(clientBuilder.build()).build();
            }
        }
        return sRetrofit;
    }

    /**
     * You can register custom type adapters to deserialize your model classes (see Retrofit documentation).
     * This creates a new instance of Retrofit as we can't modify an existing instance, so you should call this method sparingly.
     */
    public static void registerTypeAdapter(Type type, Object typeAdapter) {
        Map<Type, Object> adapters = new ConcurrentHashMap<>();
        adapters.put(type, typeAdapter);
        registerTypeAdapters(adapters);
    }

    /**
     * You can register custom type adapters to deserialize your model classes (see Retrofit documentation).
     * This creates a new instance of Retrofit as we can't modify an existing instance, so you should call this method sparingly.
     */
    public static void registerTypeAdapters(Map<Type, Object> typeAdapters) {
        // Add the given type adapters to the Gson Builder
        synchronized (ITRetrofitUtils.class) {
            if (sCustomTypeAdapters == null) {
                sCustomTypeAdapters = new HashMap<>();
            }
        }
        sCustomTypeAdapters.putAll(typeAdapters);
        for (Map.Entry<Type, Object> typeAdapter : typeAdapters.entrySet()) {
            getGsonBuilder().registerTypeAdapter(typeAdapter.getKey(), typeAdapter.getValue());
        }
        // Clear the Gson and Retrofit instance, we'll need them to be re-created
        synchronized (ITRetrofitUtils.class) {
            sGson = null;
            sRetrofit = null;
        }
    }

    /**
     * Gets or creates an instance of Gson with all the required (and custom) type adapters.
     */
    public static Gson getGson() {
        synchronized (ITRetrofitUtils.class) {
            if (sGson == null) {
                sGson = getGsonBuilder().create();
            }
        }
        return sGson;
    }

    /**
     * Creates a Gson Builder with the default type adapters required by the SDK.
     */
    private static GsonBuilder getGsonBuilder() {
        synchronized (ITRetrofitUtils.class) {
            if (sGsonBuilder == null) {
                sGsonBuilder = new GsonBuilder()
                        .registerTypeAdapter(ITAction.class, new ITAction.Deserializer())
                        .registerTypeAdapter(ITActivity.class, new ITActivity.Deserializer())
                        .registerTypeAdapter(ITClassifiedAd.class, new ITClassifiedAd.Deserializer())
                        .registerTypeAdapter(ITClassifiedAdCategory.class, new ITClassifiedAdCategory.Deserializer())
                        .registerTypeAdapter(ITContact.class, new ITContact.Deserializer())
                        .registerTypeAdapter(ITConversation.class, new ITConversation.Deserializer())
                        .registerTypeAdapter(ITData.class, new ITData.Deserializer())
                        .registerTypeAdapter(ITDeviceType.class, new ITDeviceType.Deserializer())
                        .registerTypeAdapter(ITEquipment.class, new ITEquipment.Deserializer())
                        .registerTypeAdapter(ITGreenGesture.class, new ITGreenGesture.Deserializer())
                        .registerTypeAdapter(ITLocation.class, new ITLocation.Deserializer())
                        .registerTypeAdapter(ITMessage.class, new ITMessage.Deserializer())
                        .registerTypeAdapter(ITMessagesCount.ByActivityCategory.class, new ITMessagesCount.ByActivityCategory.Deserializer())
                        .registerTypeAdapter(ITMessagesCount.ByAssetId.class, new ITMessagesCount.ByAssetId.Deserializer())
                        .registerTypeAdapter(ITNews.class, new ITNews.Deserializer())
                        .registerTypeAdapter(ITState.class, new ITState.Deserializer())
                        .registerTypeAdapter(ITStateTemplate.class, new ITStateTemplate.Deserializer())
                        .registerTypeAdapter(ITStream.class, new ITStream.Deserializer())
                        .registerTypeAdapter(ITTag.class, new ITTag.Deserializer())
                        .registerTypeAdapter(ITTagList.class, new ITTagList.Deserializer())
                        .registerTypeAdapter(ITTask.class, new ITTask.Deserializer())
                        .registerTypeAdapter(ITUser.class, new ITUser.Deserializer());
                // Register any custom type adapter
                if (sCustomTypeAdapters != null) {
                    for (Map.Entry<Type, Object> typeAdapter : sCustomTypeAdapters.entrySet()) {
                        getGsonBuilder().registerTypeAdapter(typeAdapter.getKey(), typeAdapter.getValue());
                    }
                }
            }
        }
        return sGsonBuilder;
    }

    /**
     * Returns the default base URL. For internal apps however, the base URL can be forced to another value stored in the configuration file.
     */
    private static String getBaseUrl(Context context) {
        String baseUrl = null;
        ITApp app = ITApp.getInstance(context);
        if (app != null) {
            baseUrl = app.getConfig().getString(CONFIG_BASE_URL);
        }
        if (TextUtils.isEmpty(baseUrl)) {
            baseUrl = DEFAULT_BASE_URL;
        }
        return baseUrl;
    }
}
