package eu.intent.sdk.auth

import android.content.Context
import eu.intent.sdk.api.ITApiCallback
import eu.intent.sdk.api.ITApiError
import retrofit2.Call
import java.util.concurrent.Semaphore

class ITOAuthManager(context: Context, val client: ITClient, val api: ITOAuthApi) {
    private val mSessionManager: ITSessionManager = ITSessionManager(context)
    private val mRefreshTokenSemaphore: Semaphore = Semaphore(1, true)

    /**
     * Requests a new access token from a refresh token. The received token is saved locally to be reused when calling the API.
     * This method executes synchronously, you should be careful not to block the main thread when using it.
     * @return the new access token
     */
    fun refreshToken(): ITOAuthToken? {
        var newAccessToken: ITOAuthToken? = null
        try {
            mRefreshTokenSemaphore.acquire()
            newAccessToken = api.refreshToken(ITOAuthGrantType.REFRESH.type, mSessionManager.refreshToken, client.clientId, client.clientSecret).execute().body()
            if (newAccessToken != null) {
                mSessionManager.login(newAccessToken)
            }
            mRefreshTokenSemaphore.release()
        } catch (ignored: InterruptedException) {
            // The thread has been interrupted while waiting for refreshing the token, abort (this will return null).
        }
        return newAccessToken
    }

    /**
     * Requests a new access token from a refresh token. The received token is saved locally to be reused when calling the API.
     */
    fun refreshToken(callback: ITApiCallback<ITOAuthToken>?) {
        try {
            mRefreshTokenSemaphore.acquire()
            api.refreshToken(ITOAuthGrantType.REFRESH.type, mSessionManager.refreshToken, client.clientId, client.clientSecret).enqueue(object : ITApiCallback<ITOAuthToken>() {
                override fun onFailure(call: Call<ITOAuthToken>?, body: ITApiError) {
                    mRefreshTokenSemaphore.release()
                    callback?.onFailure(call, body)
                }

                override fun onSuccess(call: Call<ITOAuthToken>?, body: ITOAuthToken) {
                    mSessionManager.login(body)
                    mRefreshTokenSemaphore.release()
                    callback?.onSuccess(call, body)
                }
            })
        } catch (ignored: InterruptedException) {
            // The thread has been interrupted while waiting for refreshing the token, abort.
        }
    }

    /**
     * Requests a new access token. The received token is saved locally to be reused when calling the API.
     */
    fun requestToken(code: String, callback: ITApiCallback<ITOAuthToken>?) {
        api.requestTokenFromCode(ITOAuthGrantType.CODE.type, code, client.clientId, client.clientSecret, client.redirectUri).enqueue(object : ITApiCallback<ITOAuthToken>() {
            override fun onFailure(call: Call<ITOAuthToken>?, body: ITApiError) {
                mSessionManager.logout()
                callback?.onFailure(call, body)
            }

            override fun onSuccess(call: Call<ITOAuthToken>?, body: ITOAuthToken) {
                mSessionManager.login(body)
                callback?.onSuccess(call, body)
            }
        })
    }

    /**
     * Requests a new access token. The received token is saved locally to be reused when calling the API.
     */
    fun requestToken(username: String, password: String, callback: ITApiCallback<ITOAuthToken>?) {
        api.requestTokenFromCredentials(ITOAuthGrantType.PASSWORD.type, username, password, client.clientId, client.clientSecret).enqueue(object : ITApiCallback<ITOAuthToken>() {
            override fun onFailure(call: Call<ITOAuthToken>?, body: ITApiError) {
                mSessionManager.logout()
                callback?.onFailure(call, body)
            }

            override fun onSuccess(call: Call<ITOAuthToken>?, body: ITOAuthToken) {
                mSessionManager.login(body)
                callback?.onSuccess(call, body)
            }
        })
    }
}