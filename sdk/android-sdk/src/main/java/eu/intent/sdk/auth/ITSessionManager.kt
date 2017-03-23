package eu.intent.sdk.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils

/**
 * A manager to work with ITSession.
 */
class ITSessionManager(context: Context) {
    companion object {
        @JvmField val ACTION_SESSION_EXPIRED = "ACTION_SESSION_EXPIRED"
    }

    private val PREF_FILE_NAME = "IntentOauth.prefs"
    private val PREF_ACCESS_TOKEN = "access_token"
    private val PREF_ACCESS_TOKEN_EXPIRY = "access_token_expiry"
    private val PREF_REFRESH_TOKEN = "refresh_token"

    private val mPrefs: SharedPreferences

    init {
        mPrefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    val accessToken: String get() = mPrefs.getString(PREF_ACCESS_TOKEN, "")
    val accessTokenExpiry: Long get() = mPrefs.getLong(PREF_ACCESS_TOKEN_EXPIRY, 0)
    val refreshToken: String get() = mPrefs.getString(PREF_REFRESH_TOKEN, "")

    /**
     * @return true if the access token can be refreshed, false otherwise
     */
    fun canRefreshToken(): Boolean = !TextUtils.isEmpty(refreshToken)

    /**
     * @return true if a user is logged in, false otherwise
     */
    fun isLoggedIn(): Boolean = !TextUtils.isEmpty(accessToken) && accessTokenExpiry > System.currentTimeMillis()

    /**
     * Saves the given token.
     */
    @SuppressLint("CommitPrefEdits")
    fun login(token: ITOAuthToken) {
        mPrefs.edit()
                .putString(PREF_ACCESS_TOKEN, token.accessToken)
                .putString(PREF_REFRESH_TOKEN, token.refreshToken)
                .putLong(PREF_ACCESS_TOKEN_EXPIRY, token.expiry)
                .commit()
    }

    /**
     * Clears the session tokens.
     */
    @SuppressLint("CommitPrefEdits")
    fun logout() {
        mPrefs.edit()
                .putString(PREF_ACCESS_TOKEN, "")
                .putString(PREF_REFRESH_TOKEN, "")
                .putLong(PREF_ACCESS_TOKEN_EXPIRY, 0)
                .commit()
    }
}