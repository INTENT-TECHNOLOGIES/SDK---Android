package eu.intent.sdk.api

import eu.intent.sdk.model.ITUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ITAccountApi {
    @GET("accounts/v1/me")
    fun me(): Call<ITUser>

    @PUT("accounts/v1/me")
    fun update(@Body body: UserUpdate): Call<ITUser>

    class UserUpdate(user: ITUser) {
        val firstname: String = user.firstName
        val lastname: String = user.lastName
        val mobile: String = user.mobile
        val phone: String = user.phone
    }
}
