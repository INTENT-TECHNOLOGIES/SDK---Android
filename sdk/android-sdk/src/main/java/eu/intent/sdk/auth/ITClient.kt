package eu.intent.sdk.auth

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import eu.intent.sdk.util.ITEnvironment
import okhttp3.HttpUrl

/**
 * An Intent client, created with your client ID and secret, environment (prod / sandbox) and the needed scopes.
 */

class ITClient(
        val environment: ITEnvironment = ITEnvironment.PRODUCTION,
        val clientId: String,
        val clientSecret: String,
        val redirectUri: String = environment.authRedirectUrl,
        vararg val scopes: String = emptyArray()
) : Parcelable {
    private val PATH_AUTHORIZE = "authorize"
    private val PARAM_CLIENT_ID = "client_id"
    private val PARAM_REDIRECT_URI = "redirect_uri"
    private val PARAM_RESPONSE_TYPE = "response_type"
    private val PARAM_RESPONSE_TYPE_CODE = "code"
    private val PARAM_SCOPES = "scope"

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ITClient> = object : Parcelable.Creator<ITClient> {
            override fun createFromParcel(source: Parcel): ITClient = ITClient(source)
            override fun newArray(size: Int): Array<ITClient?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readParcelable(ITEnvironment::class.java.classLoader), source.readString(), source.readString(), source.readString(), *source.createStringArray())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeParcelable(environment, flags)
        dest?.writeString(clientId)
        dest?.writeString(clientSecret)
        dest?.writeString(redirectUri)
        dest?.writeStringArray(scopes)
    }

    val webFormUrl: String
        get() {
            val httpUrlBuilder = HttpUrl.parse(environment.authBaseUrl).newBuilder()
                    .addPathSegment(PATH_AUTHORIZE)
                    .addQueryParameter(PARAM_RESPONSE_TYPE, PARAM_RESPONSE_TYPE_CODE)
                    .addQueryParameter(PARAM_CLIENT_ID, clientId)
                    .addEncodedQueryParameter(PARAM_REDIRECT_URI, redirectUri)
            if (scopes.isNotEmpty()) {
                httpUrlBuilder.addEncodedQueryParameter(PARAM_SCOPES, TextUtils.join(" ", scopes))
            }
            return httpUrlBuilder.build().url().toExternalForm()
        }
}
