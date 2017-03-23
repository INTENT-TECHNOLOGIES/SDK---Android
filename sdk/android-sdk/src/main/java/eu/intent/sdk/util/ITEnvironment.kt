package eu.intent.sdk.util

import android.os.Parcel
import android.os.Parcelable

/**
 * The environment your app can connect to.
 */
data class ITEnvironment(val authBaseUrl: String, val apiBaseUrl: String, val authRedirectUrl: String = "") : Parcelable {
    companion object {
        @JvmField val PRODUCTION = ITEnvironment(
                "https://accounts.hubintent.com/oauth/",
                "https://api.hubintent.com/api/",
                "https://api.hubintent.com/reference/code")
        @JvmField val SANDBOX = ITEnvironment(
                "https://accountsandbox.hubintent.com/oauth/",
                "https://apisandbox.hubintent.com/api/",
                "https://apisandbox.hubintent.com/reference/code")

        @JvmField val CREATOR: Parcelable.Creator<ITEnvironment> = object : Parcelable.Creator<ITEnvironment> {
            override fun createFromParcel(source: Parcel): ITEnvironment = ITEnvironment(source)
            override fun newArray(size: Int): Array<ITEnvironment?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(authBaseUrl)
        dest?.writeString(apiBaseUrl)
        dest?.writeString(authRedirectUrl)
    }
}