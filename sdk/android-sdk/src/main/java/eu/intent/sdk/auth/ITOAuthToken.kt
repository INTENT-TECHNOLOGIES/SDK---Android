package eu.intent.sdk.auth

import com.google.gson.annotations.SerializedName

class ITOAuthToken(
        @SerializedName("expires_in") val expiresIn: Long = 0,
        @SerializedName("access_token") val accessToken: String? = "",
        @SerializedName("refresh_token") val refreshToken: String? = "",
        @SerializedName("token_type") val tokenType: String? = ""
) {
    private val creationDate: Long = System.currentTimeMillis()

    val expiresInMs: Long get() = expiresIn * 1000
    val expiry: Long get() = creationDate + expiresInMs
}
