package eu.intent.sdk.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import eu.intent.sdk.auth.ITClient
import eu.intent.sdk.auth.ITOAuthApi
import eu.intent.sdk.auth.ITOAuthManager
import eu.intent.sdk.auth.ITSessionManager
import eu.intent.sdk.model.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * An helper to build Retrofit instance.
 */
object ITRetrofitHelper {
    fun getDefaultApiHttpClientBuilder(context: Context, client: ITClient): OkHttpClient.Builder {
        val oauthApi = getDefaultAuthRetrofit(context, client).create(ITOAuthApi::class.java)
        val oauthManager = ITOAuthManager(context, client, oauthApi)
        val sessionManager = ITSessionManager(context)
        return OkHttpClient.Builder()
                .addInterceptor(ITRetrofitGzipInterceptor())
                .addNetworkInterceptor(ITRetrofitAuthorizationInterceptor(sessionManager))
                .addNetworkInterceptor(ITRetrofitUserAgentInterceptor(context))
                .authenticator(ITRetrofitAuthenticator(context, oauthManager))
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
    }

    fun getDefaultAuthHttpClientBuilder(context: Context): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .addInterceptor(ITRetrofitGzipInterceptor())
                .addNetworkInterceptor(ITRetrofitUserAgentInterceptor(context))
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
    }

    fun getDefaultGsonBuilder(): GsonBuilder {
        return GsonBuilder()
                .registerTypeAdapter(ITActivity::class.java, ITActivity.Deserializer())
                .registerTypeAdapter(ITClassifiedAd::class.java, ITClassifiedAd.Deserializer())
                .registerTypeAdapter(ITClassifiedAdCategory::class.java, ITClassifiedAdCategory.Deserializer())
                .registerTypeAdapter(ITContact::class.java, ITContact.Deserializer())
                .registerTypeAdapter(ITConversation::class.java, ITConversation.Deserializer())
                .registerTypeAdapter(ITData::class.java, ITData.Deserializer())
                .registerTypeAdapter(ITEquipment::class.java, ITEquipment.Deserializer())
                .registerTypeAdapter(ITLocation::class.java, ITLocation.Deserializer())
                .registerTypeAdapter(ITMessage::class.java, ITMessage.Deserializer())
                .registerTypeAdapter(ITMessagesCount.ByTheme::class.java, ITMessagesCount.ByTheme.Deserializer())
                .registerTypeAdapter(ITMessagesCount.ByAssetId::class.java, ITMessagesCount.ByAssetId.Deserializer())
                .registerTypeAdapter(ITNews::class.java, ITNews.Deserializer())
                .registerTypeAdapter(ITState::class.java, ITState.Deserializer())
                .registerTypeAdapter(ITStateTemplate::class.java, ITStateTemplate.Deserializer())
                .registerTypeAdapter(ITStream::class.java, ITStream.Deserializer())
                .registerTypeAdapter(ITTag::class.java, ITTag.Deserializer())
                .registerTypeAdapter(ITTagList::class.java, ITTagList.Deserializer())
                .registerTypeAdapter(ITTicket::class.java, ITTicket.Deserializer())
                .registerTypeAdapter(ITTicketLog::class.java, ITTicketLog.Deserializer())
                .registerTypeAdapter(ITUser::class.java, ITUser.Deserializer())
    }

    fun getDefaultApiRetrofit(context: Context, client: ITClient): Retrofit {
        val httpClient = getDefaultApiHttpClientBuilder(context, client).build()
        val gson = getDefaultGsonBuilder().create()
        return getRetrofitApiBuilder(client, httpClient, gson).build()
    }

    fun getDefaultAuthRetrofit(context: Context, client: ITClient): Retrofit {
        val httpClient = getDefaultAuthHttpClientBuilder(context).build()
        val gson = GsonBuilder().create()
        return getRetrofitAuthBuilder(client, httpClient, gson).build()
    }

    fun getRetrofitApiBuilder(apiClient: ITClient, httpClient: OkHttpClient, gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(apiClient.environment.apiBaseUrl)
                .client(httpClient)
    }

    fun getRetrofitAuthBuilder(apiClient: ITClient, httpClient: OkHttpClient, gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(apiClient.environment.authBaseUrl)
                .client(httpClient)
    }
}