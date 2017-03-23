package eu.intent.sdk.auth

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ITOAuthApi {
    @FormUrlEncoded
    @POST("token")
    fun refreshToken(@Field("grant_type") grantType: String, @Field("refresh_token") refreshToken: String, @Field("client_id") clientId: String, @Field("client_secret") clientSecret: String): Call<ITOAuthToken>

    @FormUrlEncoded
    @POST("token")
    fun requestTokenFromCode(@Field("grant_type") grantType: String, @Field("code") code: String, @Field("client_id") clientId: String, @Field("client_secret") clientSecret: String, @Field("redirect_uri") redirectUri: String): Call<ITOAuthToken>

    @FormUrlEncoded
    @POST("token")
    fun requestTokenFromCredentials(@Field("grant_type") grantType: String, @Field("username") username: String, @Field("password") password: String, @Field("client_id") clientId: String, @Field("client_secret") clientSecret: String): Call<ITOAuthToken>
}
