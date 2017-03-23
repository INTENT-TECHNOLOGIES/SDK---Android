package eu.intent.sdk.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Implement this class to handle the response or failure of the Intent API.
 */
abstract class ITApiCallback<T> : Callback<T> {
    override fun onFailure(call: Call<T>?, t: Throwable?) {
        onFailure(call, ITApiError(message = t?.localizedMessage ?: ""))
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if (response == null) {
            onFailure(call, ITApiError())
        } else if (response.isSuccessful) {
            onSuccess(call, response.body())
        } else {
            val body: ITApiError = try {
                ITApiError.parseError(response)
            } catch (e: Exception) {
                ITApiError(httpCode = response.code(), httpMessage = response.message())
            }
            onFailure(call, body)
        }
    }

    /**
     * Called on request failure.
     */
    abstract fun onFailure(call: Call<T>?, body: ITApiError)

    /**
     * Called on request success.
     */
    abstract fun onSuccess(call: Call<T>?, body: T)
}